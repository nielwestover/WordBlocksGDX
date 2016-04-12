using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WordListGenerator
{
	public static class Utils
	{
		static Random rand = new Random();

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

		internal static int getMaxFrequency(int dimension)
		{
			switch (dimension)
			{
				case 3:
					return 0;
				case 4:
					return 0;
				case 5:
					return 0;
				case 6:
					return 1;
				case 7:
					return 2;
				default:
					throw new Exception("Should not have gotten here!");

			}
		}

		internal static int GetDesiredWordLength(int charsLeft)
		{
			switch(charsLeft)
			{
				case 3:
				case 4:
				case 5:
					return charsLeft;
				case 6:
					return 6;
				case 7:
					return 4;
				case 8:
				case 9:
				case 10:
				case 11:
					return rand.Next(6, charsLeft - 2);
				default:
					if (charsLeft > 11)
						return rand.Next(6, 10);
					throw new Exception("Should not have gotten here!");

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
