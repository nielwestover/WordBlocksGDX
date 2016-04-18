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
		public List<string> words
		{
			get; set;
		}
		public List<Cell> board
		{
			get; set;
		}
	}
	class Program
	{
		static void Main(string[] args)
		{
			string levels = File.ReadAllText(@"D:\allLevels.json");
			List<List<List<string>>> allLevels = JsonConvert.DeserializeObject<List<List<List<string>>>>(levels);
			List<Level> allBoards = new List<Level>();
			foreach (var levelPack in allLevels)
			{
				foreach (List<string> level in levelPack)
				{
					for (int i = 0; i < 20; i++)
					{
						Board b = generateBoard(level);
						b.print();

						//allBoards.Add(new Level() { board = b.ToList(), words = level.OrderBy(n => n).ToList() });
					}
					
				}
			}
			string all = JsonConvert.SerializeObject(allBoards);
			File.WriteAllText(@"D:\allBoards.json", all);
		}

		static Board generateBoard(List<string> level)
		{
			int dim = Utils.getDimension(level);
			Board board = new Board(dim);
			board.setDifficulty(LEVEL_DIFFICULTY.EASY);
			while (true)
			{
				board.reset();
				if (board.AddAllWords(level))
					return board;
			}
		}
	}
}
