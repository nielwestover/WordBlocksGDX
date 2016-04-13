using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace LevelGenerator
{
	class Level
	{
		public List<string> words
		{
			get; set;
		}
		public List<Cell> board
		{
			get; set;
		}
	}
	class Program
	{
		static void Main(string[] args)
		{
			string levels = File.ReadAllText(@"D:\allLevels.json");
			List<List<List<string>>> allLevels = JsonConvert.DeserializeObject<List<List<List<string>>>>(levels);
			List<Level> allBoards = new List<Level>();
			int mostWords = 0;
			foreach (var levelPack in allLevels)
			{
				foreach (List<string> level in levelPack)
				{
					Board b = generateBoard(level);
					b.print();
					if (level.Count > mostWords)
					{
						mostWords = level.Count;
						System.Console.WriteLine(mostWords);
					}
					allBoards.Add(new Level() { board = b.ToList(), words = level.OrderBy(n => n).ToList() });
				}
			}
			string all = JsonConvert.SerializeObject(allBoards);
			File.WriteAllText(@"D:\allBoards.json", all);
		}

		static Board generateBoard(List<string> level)
		{
			int dim = Utils.getDimension(level);
			Board board = new Board(dim);

			while (true)
			{
				board.reset();
				if (board.AddAllWords(level))
					return board;
			}
		}
	}
}
