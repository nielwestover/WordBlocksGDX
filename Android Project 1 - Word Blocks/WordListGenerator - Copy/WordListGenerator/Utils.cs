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
					return 1;
				default:
					throw new Exception("Should not have gotten here!");

			}
		}

		internal static int getNumWordsInLevel(int dimension, int levelNum, int numLevels)
		{
			double levelPercent = (double)levelNum / numLevels;
			int rand = RandomNumber(0, 10);
			switch (dimension)
			{
				case 3:
					if (levelPercent < .5)
						return 3;
					else
					{
						if (rand < 8)
							return 2;
						return 1;
					}
				case 4:
					{
						if (levelNum < 5)
							return 2;
						else if (levelNum < 20)
						{
							if (rand < 4)
								return 3;
							else if (rand < 8)
								return 4;
							else
								return 2;
						}
						else
						{
							if (rand < 4)
								return 3;
							else if (rand < 9)
								return 4;
							else
								return 5;
						}

					}
				case 5:					
					if (rand < 5)
						return 7;
					return 8;
				case 6:
					{
							return RandomNumber(4,9);
					
					}
				case 7:
					{
							return RandomNumber(6, 11);
						

					}
				default:
					throw new Exception("SHOULD NOT GET HERE!");

			}
		}
		public static int maxWordsForDimension(int dim)
		{
			switch (dim)
			{
				default:
					throw new Exception("SHOULD NOT GET HERE");
				case 3: return 3;
				case 4: return 5;
				case 5: return 8;
				case 6: return 11;
				case 7: return 12;
			}
		}
		public static int minWordsForDimension(int dim)
		{
			switch (dim)
			{
				default:
					throw new Exception("SHOULD NOT GET HERE");
				case 3: return 1;
				case 4: return 2;
				case 5: return 3;
				case 6: return 4;
				case 7: return 5;
			}
		}


		internal static int GetDesiredWordLength(int charsLeft, int requiredWordsInLevel, int numWordsSoFar)
		{
			if (requiredWordsInLevel == numWordsSoFar)
				return 0;
			if (requiredWordsInLevel - numWordsSoFar == 1)
			{
				if (charsLeft > 9)
					return 0;
				return charsLeft;
			}

			{
				int r = RandomNumber(0,2);
				int avgCharsPerWord = charsLeft / (requiredWordsInLevel - numWordsSoFar);
				if (avgCharsPerWord > 9)
					return 0;
				if (avgCharsPerWord == 9)
					return 9;
				avgCharsPerWord += r;//add noise
				if (avgCharsPerWord < 3) avgCharsPerWord = 3;
				if (avgCharsPerWord > 9) avgCharsPerWord = 9;

				int val = avgCharsPerWord;
				if (avgCharsPerWord > charsLeft)
					val = charsLeft;
				if (val > 9)
					val = 9;
				return val;
			}
			int rand = RandomNumber(0, 10);
			switch (charsLeft)
			{
				case 3:
					return 0;
				case 4:
				case 5:
					return charsLeft;
				case 6:
					//if (rand < 5)
					//	return 3;
					return 6;
				case 7:
					//if (rand < 3)
					//	return 3;
					if (rand < 6)
						return 4;
					return 7;
				case 8:
					//if (rand < 2) return 3;
					if (rand < 3) return 4;
					if (rand < 6) return 5;
					return 8;
				case 9:
					//if (rand < 2) return 3;
					if (rand < 3) return 4;
					if (rand < 6) return 5;
					if (rand < 8) return 6;
					return 9;
				default:
					if (charsLeft > 9)
						return RandomNumber(4, 10);					
				//default:
				//	if (charsLeft > 11)
				//	{
				//		if (rand < 2) return RandomNumber(3, 10);
				//		if (rand < 4) return RandomNumber(5, 10);
				//		if (rand < 6) return RandomNumber(6, 10);
				//		if (rand < 8) return RandomNumber(7, 10);
				//		return RandomNumber(8, 10);
				//	}
					throw new Exception("Should not have gotten here!");

			}
		}
		//Function to get random number
		private static readonly Random random = new Random(3);
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
