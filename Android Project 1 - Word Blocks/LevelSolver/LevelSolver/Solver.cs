using System.Linq;
using System.Collections.Generic;
using System;
using System.Diagnostics;

namespace LevelSolver
{

	public class Stats
	{
		public int level;
		public int boardSize;
		public int numWords;
		public int permRecursions = 0;
		public long timeForPermutation_ms;
		public int numPermutations;
		public int totalRecursions = 0;
	}
	internal class Solver
	{
		public Solver()
		{

		}
		public Stats stats = new Stats();
		List<List<string>> permutations;
		public List<int> solveBoard(Board b)
		{
			permutations = Utils.GetStringPermutations(b.words, b.words.Count);
			permutations.Shuffle();
			for (int i = 0; i < permutations.Count; ++i)
			{
				stats.permRecursions = 0;
				Stopwatch permTime = new Stopwatch();
				permTime.Start();
				List<string> curPerm = permutations[i];
				List<int> curWordOrder = new List<int>();
				Board copy = b.copy();
				copy.words = new List<string>(curPerm);
				copy.curCell = null;
				var solveOrder = solve(copy);
				if (solveOrder != null)
				{
					stats.timeForPermutation_ms = permTime.ElapsedMilliseconds;
					stats.numPermutations = i + 1;
					stats.numWords = b.words.Count;
					stats.boardSize = b.dim;
					return solveOrder;
				}
				permTime.Stop();

			}
			return null;
		}

		public List<int> solve(Board b)
		{
			stats.permRecursions++;
			stats.totalRecursions++;
			//b.print();
			if (String.IsNullOrEmpty(b.words[0]))
			{
				b.words.RemoveAt(0);
				b.curCell = null;//starting new word
				b.dropWords();
			}
			//first check if end condition is satisfied
			if (b.words.Count == 0)
			{
				return b.solveOrder;
			}
			//candidate is first letter of first word
			string candidate = b.words[0].Substring(0, 1);

			List<RowColPair> startLocations;
			//new word, look at all candidate locations
			if (b.curCell == null)
			{
				b.curWord = b.words[0];
				b.curLetterIndex = 0;
				startLocations = b.getLetterLocations(candidate);
			}
			//not new word, look at all letters around curCell
			else
			{
				++b.curLetterIndex;
				startLocations = b.getLetterLocationsNearby(candidate, b.curCell);
			}
			//remove dead-end paths
			//startLocations = b.removeBadLocations(startLocations);

			foreach (RowColPair item in startLocations)
			{
				Board newBoard = Utils.GetNewBoardState(b, item);
				List<int> solution = solve(newBoard);
				if (solution != null)
					return solution;
			}
			return null;
		}


	}
}