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

        private static void SolveTime(List<string> level, Solver s)
        {
            Console.WriteLine("\r\n");

            double elapsed = 0;
            Stopwatch sw = new Stopwatch();
            sw.Start();
            Level l = getLevelFromList(level);
            Board b = new Board(l);
            b.EmptyToNullCell();
            List<int> solution = s.solveBoard(b);

            double el = sw.ElapsedMilliseconds / 1000.0;
            elapsed += el;
            if (solution == null)
                Console.WriteLine(el + " seconds" + " - NO SOLUTION");
            else
                Console.WriteLine(el + " seconds");
            sw.Restart();
            Console.WriteLine("TOTAL: " + elapsed + " seconds");
            Console.WriteLine("AVG: " + elapsed / iterations + " seconds");
        }
       
        private static Level getLevelFromList(List<string> level)
        {
            Level l = new Level();
            l.words = level;

        }
        public static String ReverseString(String s)
        {
            return new StringBuilder(s).reverse().toString();
        }


        private static Level generateLevel(List<String> level, long seed)
        {
            Board board = new Board(Utils.getDimension(level), seed);
            do
            {
                board.reset();
            } while (!board.AddAllWords(level));
            Level l = new Level();
            l.board = board.ToList();
            l.words = level;
            return l;
        }
        public static int getDimension(List<String> level)
        {
            int count = 0;
            for (int i = 0; i < level.size(); i++)
            {
                count += ((String)level.get(i)).length();
            }
            return (int)Math.sqrt((double)count);
        }
        static void Main(string[] args)
        {

            //string levels = File.ReadAllText(@"D:\solverTest.json");
            string levels = File.ReadAllText(@"D:\Git\WordBlocksGDX\android\assets\completePackList.json");
            List<LevelPack> allLevels = JsonConvert.DeserializeObject<List<LevelPack>>(levels);
            List<Stats> allStats = new List<Stats>();
            //SolveTime(allLevels[0], new SolverShuffle(), "SHUFFLED");
            //SolveTime(allLevels[0], new SolverPerm(), "PERMUTATIONS");
            //SolveTime(allLevels[0], new SolverPaths(), "SHUFFLED WITH HOR PATHS");
            foreach (var pack in allLevels)
            {
                foreach (var level in pack.levels)
                {
                    SolveTime(level, new SolverPermsOnFly());
                }
            }

            Console.ReadLine();
        }
    }
}
