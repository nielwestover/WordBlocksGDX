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

		List<List<string>> levelList;
		public List<List<string>> Generate(int dimension, int numLevels)
		{
			levelList = new List<List<string>>();
			for (int i = 0; i < numLevels; ++i)
			{
				int maxChars = dimension * dimension;
				List<string> level = GenerateLevel(maxChars);
				while (level == null || level.Count == 0)
				{
					level = GenerateLevel(maxChars);
				}
				levelList.Add(level);
				System.Console.Write(i + ": ");
				Utils.printLevel(level);
			}
			return levelList;
		}

		private List<string> GenerateLevel(int maxChars)
		{
			List<string> levelWords = new List<string>();
			string finalChars = "";
			string curWord = "";
			int desiredLength;
			int count = 0;
			while (finalChars.Length != maxChars && ++count < maxChars * 100)
			{
				desiredLength = Utils.GetDesiredWordLength(maxChars - finalChars.Length);
				curWord = getRandomWordOfSpecifiedLength(desiredLength);

				while (levelWords.Contains(curWord))
					curWord = getRandomWordOfSpecifiedLength(desiredLength);
				int newLength = curWord.Length + finalChars.Length;
				if (newLength == maxChars || newLength + 3 <= maxChars)
				{
					//see if it's a good word to add to the level
					if (Utils.wordMatchesLevel(curWord, levelWords, finalChars))
					{
						if (wordAdheresToQuality(levelWords, curWord))
						{
							levelWords.Add(curWord);
							finalChars += curWord;
						}
					}
				}
			}
			if (finalChars.Length != maxChars)
				return null;
			if (levelWords.Count == 7 || levelWords.Count == 8 || levelWords.Count == 9)
				return null;
			markUsed(levelWords);
			return levelWords;
		}

		private bool wordAdheresToQuality(List<string> levelWords, string curWord)
		{
			List<string> newList = new List<string>(levelWords);
			newList.Add(curWord);
			return isQualityLevel(levelWords);
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
					if (beginCount > 1 || endCount > 1)
					{
						//Console.WriteLine("Rejecting " + string.Join(" ", levelWords.ToArray()));
						return false;
					}
				}
			}
			foreach (var item in levelWords)
			{				
				string begin = item.Substring(0, 3);
				string end = item.Substring(item.Length - 3, 3);
				int beginCount = 0;
				int endCount = 0;
				foreach (var word in levelWords)
				{					
					string sub1 = word.Substring(0, 3);
					if (sub1.Equals(begin))
						beginCount++;
					string sub2 = word.Substring(word.Length - 3, 3);
					if (sub2.Equals(end))
						endCount++;
					if (beginCount > 1 || endCount > 1)
					{
						//Console.WriteLine("Rejecting " + string.Join(" ", levelWords.ToArray()));
						return false;
					}
				}
			}
			return true;
		}

	private void markUsed(List<string> levelWords)
	{
		foreach (var item in levelWords)
		{
			words[item.Length - 3].Remove(item);
		}
	}

	private string getRandomWordOfSpecifiedLength(int desiredLength)
	{
		//desiredLength 3 -> list at index 0
		//desiredLength 4 -> list at index 1
		//hence desiredLength -3
		{
			int index = Utils.RandomNumber(0, words[desiredLength - 3].Count);
			string word = words[desiredLength - 3][index];
			return word;
		}
	}
}
}
