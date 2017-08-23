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

        private static void SolveTime(Level level, Solver s, string name)
        {
            Console.WriteLine("\r\n");
            Console.WriteLine(name);
            double elapsed = 0;
            int iterations = 5;
            for (int i = 0; i < iterations; ++i)
            {
                Stopwatch sw = new Stopwatch();
                sw.Start();

                Board b = new Board(level);
                b.EmptyToNullCell();
                List<int> solution = s.solveBoard(b);

                double el = sw.ElapsedMilliseconds / 1000.0;
                elapsed += el;
                if (solution == null)
                    Console.WriteLine(el + " seconds" + " - NO SOLUTION");
                else
                    Console.WriteLine(el + " seconds");
                sw.Restart();
            }
            Console.WriteLine("TOTAL: " + elapsed + " seconds");
            Console.WriteLine("AVG: " + elapsed/iterations + " seconds");
        }

        static void Main(string[] args)
		{

            //string levels = File.ReadAllText(@"D:\solverTest.json");
            string levels = File.ReadAllText(@"D:\solverTestPart.json");
            List<Level> allLevels = JsonConvert.DeserializeObject< List < Level >> (levels);
			List<Stats> allStats = new List<Stats>();
            //SolveTime(allLevels[0], new SolverShuffle(), "SHUFFLED");
            SolveTime(allLevels[0], new SolverPerm(), "PERMUTATIONS");
            //SolveTime(allLevels[0], new SolverPaths(), "SHUFFLED WITH HOR PATHS");

            Console.ReadLine();		
		}
	}
}
