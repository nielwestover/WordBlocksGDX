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
			ListGen listGen = new ListGen(@"D:\Git\Android Project 1 - Word Blocks\4000 words.json");
			List<List<List<string>>> allLevels = new List<List<List<string>>>();
			allLevels.Add(listGen.Generate(3, 20));
			allLevels.Add(listGen.Generate(4, 100));
			allLevels.Add(listGen.Generate(5, 100));
			allLevels.Add(listGen.Generate(6, 150));
			allLevels.Add(listGen.Generate(7, 150));
			System.Console.WriteLine("TOTAL WORDS USED: " + listGen.freq.Count);
			string allText = JsonConvert.SerializeObject(allLevels);
			File.WriteAllText("D:/allLevels.json", allText);
			Console.ReadLine();
		}
	}
}
