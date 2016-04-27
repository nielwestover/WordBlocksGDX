using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using WordListGenerator;

namespace ListGeneratorConsole
{
	class Program
	{
		static void Main(string[] args)
		{
			List<List<string>> words_4000 = JsonConvert.DeserializeObject<List<List<string>>>(File.ReadAllText(@"D:\Git\WordBlocksGDX\Android Project 1 - Word Blocks\4000 words.json"));

			List<List<List <string>>> usedWords = JsonConvert.DeserializeObject< List<List<List<string>>>>(File.ReadAllText(@"D:\WORD\usedWords7.json"));
			//words_4000 = removeUsedWords(usedWords, words_4000);

			ListGen listGen = new ListGen(words_4000);
			List<List<List<string>>> allLevels = new List<List<List<string>>>();
			allLevels.Add(listGen.Generate(7, 250));
			//allLevels.Add(listGen.Generate(4, 100));
			//allLevels.Add(listGen.Generate(5, 15));
			//allLevels.Add(listGen.Generate(6, 160));
			//allLevels.Add(listGen.Generate(7, 180));
			string allText = JsonConvert.SerializeObject(allLevels);
			//File.WriteAllText("D:/allLevelsSoFar3-5.json", allText);
			//allText = JsonConvert.SerializeObject(allLevels);
			//File.WriteAllText("D:/allLevelsSoFar6.json", allText);
			System.Console.WriteLine("TOTAL WORDS USED: " + listGen.freq.Count);
			//allText = JsonConvert.SerializeObject(allLevels);
			File.WriteAllText("D:/allLevelsSoFar.json", allText);
			Console.ReadLine();
		}

		private static List<List<string>> removeUsedWords(List<List<List<string>>> usedWords, List<List<string>> words_4000)
		{
			foreach (var dimLevel in usedWords)
			{
				foreach (var level in dimLevel)
				{
					foreach (string usedWord in level)
					{
						removeWordFrom4000(usedWord, words_4000);
					}

				}
			}
			return words_4000;
		}

		private static void removeWordFrom4000(string usedWord, List<List<string>> words_4000)
		{
			foreach (List<string> dimLevel in words_4000)
			{
				if (dimLevel.Contains(usedWord))
				{
					dimLevel.Remove(usedWord);
					return;
				}
				
			}
		}
	}
}
