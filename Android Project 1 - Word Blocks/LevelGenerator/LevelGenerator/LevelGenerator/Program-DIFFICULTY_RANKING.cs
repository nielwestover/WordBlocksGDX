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
		[JsonIgnore]
		public List<string> words
		{
			get; set;
		}
		[JsonIgnore]
		public List<Cell> board
		{
			get; set;
		}

		public string wordsString { get; set; }
		public List<int> spread
		{
			get; set;
		}

	}
	class Program
	{
		static void Main(string[] args)
		{
			string levels = File.ReadAllText(@"D:\allLevelsInline.json");
			//string levels = File.ReadAllText(@"D:\sampleWords.json");
			List<List<string>> allLevels = JsonConvert.DeserializeObject<List<List<string>>>(levels);
			List<Level> allBoards = new List<Level>();
			for (int k = 0; k < allLevels.Count; ++k)
				allBoards.Add(new Level());
			for (int i = 0; i < 20; i++)
			{
				Utils.setRandomSeed(i);

				int levelNum = 0;

				int numLevels = allLevels.Count;

				for (int k = 0; k < allLevels.Count; ++k)
				{
					List<String> level = allLevels[k];
					Board b = generateBoard(level, .5);// .85*(i/numLevels) + .10);
					b.print();

					allBoards[k].wordsString = string.Join(" ", level.ToArray());
					if (allBoards[k].spread == null)
						allBoards[k].spread = new List<int>();
					allBoards[k].spread.Add(b.levelSpread);
					Console.WriteLine("seed: " + i + "  level: " + k + "  spread:" + b.levelSpread);
					//allBoards.Add(new Level() { board = b.ToList(), words = level.OrderBy(n => n).ToList(), spread = b.levelSpread, wordsString = string.Join(" ", level.ToArray()) });
				}				
			}

			string all = JsonConvert.SerializeObject(allBoards);
			File.WriteAllText(@"D:\spreads.json", all);
		}

		static Board generateBoard(List<string> level, double difficulty)
		{
			int dim = Utils.getDimension(level);
			Board board = new Board(dim, difficulty);
			while (true)
			{
				board.reset();
				if (board.AddAllWords(level))
					return board;
			}
		}
	}
}
