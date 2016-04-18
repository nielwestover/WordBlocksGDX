using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LevelGenerator
{
	public class Utils
	{

		public static int getDimension(List<string> level)
		{
			int count = 0;
			foreach (var item in level)
			{
				count += item.Length;
			}
			return (int)Math.Sqrt(count);
		}

		//Function to get random number
		private static readonly Random random = new Random(0);
		private static readonly object syncLock = new object();
		public static int RandomNumber(int min, int max)
		{
			lock (syncLock)
			{ // synchronize
				return random.Next(min, max);
			}
		}

		public static string ReverseString(string s)
		{
			char[] arr = s.ToCharArray();
			Array.Reverse(arr);
			return new string(arr);
		}

		public static int minLevelSpread(int dim, LEVEL_DIFFICULTY levelDifficulty)
		{
			throw new NotImplementedException();
		}

		public static int maxLevelSpread(int dim, LEVEL_DIFFICULTY levelDifficulty)
		{
			throw new NotImplementedException();
		}

	}
}
