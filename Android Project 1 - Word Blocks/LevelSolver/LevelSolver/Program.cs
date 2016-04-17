using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace LevelSolver
{
	class Program
	{
		static void Main(string[] args)
		{
			Stopwatch sw = new Stopwatch();
			sw.Start();
			string levels = File.ReadAllText(@"D:\allBoards.json");
			List<Level> allLevels = JsonConvert.DeserializeObject< List < Level >> (levels);

			foreach (var level in allLevels)
			{
				Solver s = new Solver();
				Board b = new Board(level);

				List<int> solution = s.solveBoard(b);
				Console.Write("Length: " + level.board.Count + "  Words: ");
				foreach (var item in level.words)
				{
					Console.Write(item + " ");
				}
				foreach (var item in solution)
				{
					Console.Write(item + " ");
				}
				Console.WriteLine();
			}
			Console.WriteLine(sw.ElapsedMilliseconds / 1000.0 + " seconds");
			Console.ReadLine();		
		}
	}
}
