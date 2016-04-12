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
		public ListGen(string file)
		{
			string allWords = File.ReadAllText(file);
			words = JsonConvert.DeserializeObject<List<List<string>>>(allWords);
		}
		Random rand = new Random();

		List<List<string>> levelList;
		public List<List<string>> Generate(int dimension, int numLevels)
		{
			levelList = new List<List<string>>();
			int frequency = 0;
			for (int i = 0; i < numLevels; ++i)
			{
				int maxChars = dimension * dimension;
				frequency = 0;
				List<string> level = GenerateLevel(maxChars, frequency);
				while (level == null || level.Count == 0)
				{
					++frequency;
					int maxFreq = Utils.getMaxFrequency(dimension);
					if (dimension == 7 && levelList.Count > 170)
						maxFreq = 2;
					level = GenerateLevel(maxChars, Math.Min(maxFreq, frequency));
				}
				levelList.Add(level);
				System.Console.Write(i + ": ");
				Utils.printLevel(level);
			}
			System.Console.WriteLine("Words used: " + freq.Count);
			return levelList;
		}

		private List<string> GenerateLevel(int maxChars, int frequency)
		{
			List<string> levelWords = new List<string>();
			string finalChars = "";
			string curWord = "";
			int desiredLength;
			int count = 0;
			while (finalChars.Length != maxChars && ++count < maxChars * 5000)
			{
				if (maxChars == 9)
					desiredLength = 3;
				else
					desiredLength = Utils.GetDesiredWordLength(maxChars - finalChars.Length);
				curWord = getRandomWordOfSpecifiedLengthAndFrequency(desiredLength, frequency);
				
				if (levelWords.Contains(curWord))
					continue;
				int newLength = curWord.Length + finalChars.Length;
				if (newLength == maxChars || newLength + 3 <= maxChars)
				{
					//see if it's a good word to add to the level
					if (Utils.wordMatchesLevel(curWord, levelWords, finalChars))
					{
						levelWords.Add(curWord);
						finalChars += curWord;
					}
				}
			}
			if (finalChars.Length != maxChars)
				return null;
			markUsed(levelWords);
			return levelWords;
		}

		private void markUsed(List<string> levelWords)
		{
			foreach (string item in levelWords)
			{
				if (freq.ContainsKey(item))
					freq[item]++;
				else
					freq[item] = 1;
			}
		}

		private string getRandomWordOfSpecifiedLengthAndFrequency(int desiredLength, int frequency)
		{
			//desiredLength 3 -> list at index 0
			//desiredLength 4 -> list at index 1
			//hence desiredLength -3
			while (true)
			{
				int index = rand.Next(0, words[desiredLength - 3].Count);
				string word = words[desiredLength - 3][index];
				if (!freq.ContainsKey(word))
					return word;
				if (freq[word] <= frequency)
					return word;
			}
		}
	}
}
