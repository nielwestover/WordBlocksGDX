using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WordListGenerator
{
	public static class Utils
	{
		internal static bool wordMatchesLevel(string curWord, List<string> levelWords, string allChars)
		{
			if (levelWords.Count == 0)
				return true;
			return (wordsGoodMatch(levelWords, curWord) && wordsHaveUniqueLetters(allChars, curWord));

		}

		private static bool wordsHaveUniqueLetters(string allChars, string curWord)
		{
			if (allChars.Length == 0)
				return true;
			for (int i = 0; i < curWord.Length; ++i)
			{
				if (!allChars.Contains(curWord[i]))
					return true;
			}
			return false;
		}

		private static bool wordsGoodMatch(List<string> levelWords, string word)
		{
			for (int i = 0; i < levelWords.Count; ++i)
			{
				int numMatched = numMatchedCharacters(levelWords[i], word);
				int required = (int)(.9 * word.Length);
				if (numMatched < required)
					return false;
			}
			return true;

		}

		internal static int GetDesiredWordLength(int charsLeft)
		{
			int rand = RandomNumber(0, 10);
			
			switch (charsLeft)
			{
				case 3:
				case 4:
				case 5:
					return charsLeft;
				case 6:					
					return 6;
				case 7:
					if (rand < 5)
						return 4;
					return 7;
				case 8:					
					if (rand < 4) return 4;
					if (rand < 5) return 5;
					return 8;
				case 9:
					if (rand < 3) return 4;
					if (rand < 3) return 5;
					if (rand < 6) return 6;
					return 9;
				case 10:
				case 11:
					if (rand < 1) return 3;
					if (rand < 2) return 4;
					if (rand < 3) return 5;
					if (rand < 6) return 6;
					return 7;
				default:
					if (charsLeft > 11)
					{
						//if (rand < 2) return RandomNumber(3, 10);
						//if (rand < 4) return RandomNumber(5, 10);
						//if (rand < 6) return RandomNumber(6, 10);
						//if (rand < 8) return RandomNumber(7, 10);
						return RandomNumber(4, 10);
					}
					throw new Exception("Should not have gotten here!");

			}
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
		internal static void printLevel(List<string> level)
		{
			foreach (var item in level)
			{
				System.Console.Write(item + " ");
			}
			System.Console.WriteLine();
		}

		private static int numMatchedCharacters(string allChars, string word)
		{
			int numMatched = 0;
			for (int i = 0; i < word.Length; ++i)
			{
				if (allChars.Contains(word[i]))
				{
					numMatched++;
					continue;
				}
			}
			return numMatched;
		}
	}
}
