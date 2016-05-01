using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LevelSolver
{

	public class RowColPair
	{
		public RowColPair() { }
		public RowColPair(int first, int second)
		{
			this.Row = first;
			this.Col = second;
		}

		public int Row { get; set; }
		public int Col { get; set; }

		public override string ToString()
		{
			return Row + ", " + Col;
		}

		internal bool equals(int row, int col)
		{
			return Row == row && Col == col;
		}
	};
	static class Extensions
	{
		public static IList<T> Clone<T>(this IList<T> listToClone) where T : ICloneable
		{
			return listToClone.Select(item => (T)item.Clone()).ToList();
		}
	}
	static class Utils
	{
		private static Random rand = new Random(0);

		public static void Shuffle<T>(this IList<T> list)
		{
			int n = list.Count;
			while (n > 1)
			{
				n--;
				int k = rand.Next(n + 1);
				T value = list[k];
				list[k] = list[n];
				list[n] = value;
			}
		}

		static IEnumerable<IEnumerable<T>> GetPermutations<T>(IEnumerable<T> list, int length)
		{
			if (length == 1) return list.Select(t => new T[] { t });

			return GetPermutations(list, length - 1)
				.SelectMany(t => list.Where(e => !t.Contains(e)),
					(t1, t2) => t1.Concat(new T[] { t2 }));
		}

		static public List<List<string>> GetStringPermutations(List<string> list, int length)
		{
			List<List<string>> perms = new List<List<string>>();

			var p = GetPermutations(list, length);
			var listPerms = p.ToList();
			foreach (var item in listPerms)
			{
				perms.Add(item.ToList());
			}
			return perms;
		}

		internal static Board GetNewBoardState(Board board, RowColPair item)
		{
			//Make a copy of the board we can manipulate
			Board b = board.copy();

			//Remove the first letter and add it to the solve order
			b.words[0] = board.words[0].Remove(0, 1);
			b.solveOrder.Add(board.getCell(item.Row, item.Col).id);

			//Make curCell point to location of letter we just processed
			b.curCell = item;

			return b;
		}
	}
}
