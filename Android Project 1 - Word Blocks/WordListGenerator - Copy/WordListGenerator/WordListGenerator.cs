using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace WordListGenerator
{
	public class ListGen
	{
		public Dictionary<string, int> freq = new Dictionary<string, int>();
		private List<List<string>> words;		
		public ListGen(List<List<string>> allWords)
		{
			words = allWords;
		}
		int requiredWordsInLevel;
        List<List<string>> levelList;
		int dimension;
		public List<List<string>> Generate(int dimension, int numLevels)
		{
			this.dimension = dimension;
			levelList = new List<List<string>>();
			int frequency = 0;
			for (int i = 0; i < numLevels; ++i)
			{
				int maxChars = dimension * dimension;
				frequency = 0;
				requiredWordsInLevel = Utils.getNumWordsInLevel(dimension, i, numLevels);
				List<string> level = GenerateLevel(maxChars, frequency, i);
				while (level == null || level.Count == 0 || !isQualityLevel(level))
				{
					//++frequency;
					level = GenerateLevel(maxChars, 0, i);
				}
				levelList.Add(level);
				System.Console.Write(i + ": ");
				Utils.printLevel(level);
			}
			System.Console.WriteLine("Words used: " + freq.Count);
			return levelList;
		}

		private bool isHighQualityLevel(List<string> level, int dimension, int levelNum, int numLevels)
		{		
				
			return requiredWordsInLevel == level.Count;				
		}
		private bool isQualityLevel(List<string> levelWords)
		{
			foreach (var item in levelWords)
			{
				string begin = item.Substring(0, item.Length - 1);
				string end = item.Substring(1, item.Length - 1);
				int beginCount = 0;
				int endCount = 0;
				foreach (var word in levelWords)
				{
					if (item.Length != word.Length)
						continue;
					string sub1 = word.Substring(0, begin.Length);
					if (sub1.Equals(begin))
						beginCount++;
					string sub2 = word.Substring(1, end.Length);
					if (sub2.Equals(end))
						endCount++;
					if (beginCount > 2 || endCount > 2)
					{
						//Console.WriteLine("Rejecting " + string.Join(" ", levelWords.ToArray()));
						return false;
					}
				}
			}
			int count = 0;
			foreach (var item in levelWords)
			{
				if (item.EndsWith("TION"))
					++count;
				if (count > 2)
				{
					//Console.WriteLine("Rejecting " + string.Join(" ", levelWords.ToArray()));
					return false;
				}
			}
			return true;
		}
		public int outerCount = 0;
		private List<string> GenerateLevel(int maxChars, int frequency, int levelNum)
		{
			List<string> levelWords = new List<string>();
			string finalChars = "";
			string curWord = "";
			int desiredLength;
			int count = 0;

			
			while (finalChars.Length != maxChars && ++count < maxChars * 500)
			{				
				desiredLength = Utils.GetDesiredWordLength(maxChars - finalChars.Length, requiredWordsInLevel, levelWords.Count);
				if (desiredLength == 0)
					return null;
				curWord = getRandomWordOfSpecifiedLengthAndFrequency(desiredLength, frequency);
				
				while (levelWords.Contains(curWord))
					curWord = getRandomWordOfSpecifiedLengthAndFrequency(desiredLength, frequency);
				int newLength = curWord.Length + finalChars.Length;
				if (newLength == maxChars || newLength + 3 <= maxChars)
				{
					//see if it's a good word to add to the level
					if (Utils.wordMatchesLevel(curWord, levelWords, finalChars))
					{
						levelWords.Add(curWord);
						finalChars += curWord;
						//if (finalChars.Length > 40)
						//	Console.Write('+');
					}
				}
			}
			if (finalChars.Length != maxChars)
			{
				if (outerCount++ > 100)
				{
					outerCount = 0;
					requiredWordsInLevel = Utils.getNumWordsInLevel(dimension, levelNum, 100);
					//Console.Write(requiredWordsInLevel + ".");
				}
				return null;
			}
			markUsed(levelWords);
			outerCount = 0;
			return levelWords;
		}

		private void markUsed(List<string> levelWords)
		{
			foreach (string item in levelWords)
			{
				words[item.Length - 3].Remove(item);

			}

		}

		private string getRandomWordOfSpecifiedLengthAndFrequency(int desiredLength, int frequency)
		{
			//desiredLength 3 -> list at index 0
			//desiredLength 4 -> list at index 1
			//hence desiredLength -3
			while (true)
			{
				int index = Utils.RandomNumber(0, words[desiredLength - 3].Count);
				string word = words[desiredLength - 3][index];
				return word;
			}
		}
	}
}
