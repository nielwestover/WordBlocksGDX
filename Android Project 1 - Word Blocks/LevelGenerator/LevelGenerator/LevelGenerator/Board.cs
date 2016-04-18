using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace LevelGenerator
{
	public enum LEVEL_DIFFICULTY
	{
		EASY,
		MEDIUM,
		HARD
	};

	public class RowColPair
	{
		public RowColPair() { }
		public RowColPair(int first, int second)
		{
			this.Row = first;
			this.Col = second;
		}

		public int Row { get; set; }
		public int Col { get; set; }

		public override string ToString()
		{
			return Row + ", " + Col;
		}

		internal bool equals(RowColPair p)
		{
			return Row == p.Row && Col == p.Col;
		}
	};
	public class Cell
	{

		public string c
		{
			get; set;
		}
		public int h { get; set; }
		[JsonIgnore]
		public string Word
		{
			get; set;
		}
		[JsonIgnore]
		public RowColPair loc;
	}
	public class Board
	{
		Cell[,] board;
		public List<Cell> ToList()
		{
			List<Cell> l = new List<Cell>();
			for (int row = 0; row < dim; ++row)
			{
				for (int col = 0; col < dim; ++col)
				{
					l.Add(board[row, col]);
				}
			}
			return l;
		}

		internal bool AddAllWords(List<string> level)
		{
			foreach (var word in level)
			{
				if (!insertWord(word))
					return false;
			}
			return isQualityBoard(level);
		}

		private bool isQualityBoard(List<string> level)
		{
			List<Cell> solveOrder = getSolveOrder();
			
			foreach (var item in level)
			{
				if (item.Length <= dim && wordEasyToFind(item))
					return false;
				
			}
			int levelSpread = getLevelSpread(solveOrder);
			Console.WriteLine(string.Join(" ", level.ToArray()) + "\t\tspread = " + levelSpread);
			
			//if (levelSpread < Utils.minLevelSpread(dim, levelDifficulty))
			//	return false;
			//if (levelSpread > Utils.maxLevelSpread(dim, levelDifficulty))
			//	return false;

			return true;
		}

		private int getLevelSpread(List<Cell> solveOrder)
		{
			int spread = 0;
			while (solveOrder.Count > 0)
			{
				int wordSize = solveOrder.First().Word.Length;
				List<Cell> curWordList = solveOrder.GetRange(0, wordSize);

				spread += recursiveGetSpread(curWordList, null, 0, 0);

				solveOrder.RemoveRange(0, wordSize);
				dropWords(solveOrder);
			}
			return spread;
		}
		public void dropWords(List<Cell> solveOrder)
		{
			Cell[,] b = new Cell[dim, dim];
			foreach (var item in solveOrder)
			{
				b[item.loc.Row, item.loc.Col] = item;
			}
			
			bool blockDropped = true;
			while (blockDropped)
			{
				blockDropped = false;
				for (int col = 0; col < dim; ++col)
				{
					for (int row = 0; row < dim - 1; ++row)
					{
						if (b[row, col] == null)
						{
							if (row + 1 < dim && b[row + 1, col] != null)
							{
								b[row, col] = b[row + 1, col];
								b[row + 1, col] = null;
								blockDropped = true;
							}
						}
					}
				}
			}
			for (int col = 0; col < dim; ++col)
			{
				for (int row = 0; row < dim - 1; ++row)
				{
					if (b[row, col] != null)
					{
						b[row, col].loc.Col = col;
						b[row, col].loc.Row = row;
					}
				}
			}
			
		}

		private int recursiveGetSpread(List<Cell> curWordList, RowColPair dir, int lastScore, int spreadSum)
		{
			if (curWordList.Count == 1)
				return spreadSum;
			spreadSum += getSpreadForLetterPair(curWordList[0], curWordList[1], ref dir, ref lastScore);
			curWordList.RemoveAt(0);
			return recursiveGetSpread(curWordList, dir, lastScore, spreadSum);
		}

		private int getSpreadForLetterPair(Cell cell1, Cell cell2, ref RowColPair dir, ref int lastScore)
		{
			int rowDiff = cell1.loc.Row - cell2.loc.Row;
			int colDiff = cell1.loc.Col - cell2.loc.Col;
			RowColPair newDir = new RowColPair(rowDiff, colDiff);
			bool isDiag = Math.Abs(newDir.Col) == 1 && Math.Abs(newDir.Row) == 1;
			if (dir == null)
			{
				lastScore = (isDiag ? 5 : 1);
				dir = newDir;
			}
			else
			{
				if (dir.equals(newDir))
					lastScore--;
			}
			return lastScore;
		
		}

		private List<Cell> getSolveOrder()
		{
			Cell[] l = new Cell[dim * dim];
			for (int row = 0; row < dim; ++row)
			{
				for (int col = 0; col < dim; ++col)
				{
					board[row, col].loc = new RowColPair(row, col);
					l[board[row, col].h] = board[row, col];
				}
			}
			return l.ToList();
		}


		LEVEL_DIFFICULTY levelDifficulty = LEVEL_DIFFICULTY.EASY;
		internal void setDifficulty(LEVEL_DIFFICULTY difficulty)
		{
			levelDifficulty = difficulty;
		}

		private bool wordEasyToFind(string item)
		{
			for (int row = 0; row < dim; ++row)
			{
				string rowWord = "";
				for (int col = 0; col < dim; ++col)
				{
					rowWord += board[row, col].c;
				}
				if (rowWord.Contains(item) || Utils.ReverseString(rowWord).Contains(item))
					return true;
			}
			for (int col = 0; col < dim; ++col)
			{
				string colWord = "";
				for (int row = 0; row < dim; ++row)
				{
					colWord += board[row, col].c;
				}
				if (colWord.Contains(item) || Utils.ReverseString(colWord).Contains(item))
					return true;
			}
			return false;
		}

		int dim;

		public Board(int dim)
		{
			this.dim = dim;
			board = new Cell[dim, dim];
		}

		public void reset()
		{
			hintIndex = dim * dim - 1;
			for (int row = 0; row < dim; ++row)
			{
				for (int col = 0; col < dim; ++col)
				{
					board[row, col] = null;
				}
			}
		}

		public void print()
		{
			//row
			for (int row = dim - 1; row >= 0; --row)
			{
				//col
				for (int col = 0; col < dim; ++col)
				{
					if (board[row, col] != null)
						System.Console.Write(board[row, col].c);
					else
						System.Console.Write(".");
				}
				System.Console.WriteLine();
			}
			System.Console.WriteLine();
		}

		internal bool insertWord(string word)
		{
			//start with a random location
			RowColPair cell = new RowColPair(Utils.RandomNumber(0, dim), Utils.RandomNumber(0, dim));
			//we're building the level from the final state to the initial state, so iterate over the letters in reverse order
			for (int i = word.Length - 1; i >= 0; --i)
			{
				cell = getNextCell(cell, word);
				//Got into a bad state, so bail and start over!
				if (cell == null)
					return false;
				insertCharacter(word[i], word, cell);
			}
			return true;
		}

		int hintIndex;

		private void insertCharacter(char ch, string word, RowColPair cell)
		{
			if (board[cell.Row, cell.Col] != null)
			{
				//raise the characters to make room
				int curRow = dim - 1;
				while (curRow != cell.Row)
				{
					if (board[curRow, cell.Col] == null && board[curRow - 1, cell.Col] != null)
					{
						board[curRow, cell.Col] = board[curRow - 1, cell.Col];
						board[curRow - 1, cell.Col] = null;
					}
					--curRow;
				}
			}
			board[cell.Row, cell.Col] = new Cell() { c = ch.ToString(), Word = word, h = hintIndex-- };

		}

		private RowColPair getNextCell(RowColPair cell, string word)
		{
			int count = 0;
			while (++count < 50)
			{
				RowColPair newCell = getRandomAdjacentCell(cell);
				if (cellIsValid(newCell, word))
					return newCell;
			}
			return null;
		}

		private bool cellIsValid(RowColPair cell, string word)
		{
			//check it's inside grid
			if (cell.Row >= dim || cell.Row < 0 || cell.Col >= dim || cell.Col < 0)
				return false;

			//check there is no null space immediately below the cell
			// -- you can only build up on top of other blocks
			if (cell.Row > 0 && board[cell.Row - 1, cell.Col] == null)
				return false;

			//can push up ONLY if there is space - if top space is null, then there's room.  If not, return false
			if (board[dim - 1, cell.Col] != null)
				return false;
			//AND if no Cells above contain the current word (or it will be unsolvable!)
			for (int row = dim - 1; row >= cell.Row; --row)
			{
				//null cells are good for business, move along
				if (board[row, cell.Col] == null)
					continue;
				//not valid!
				if (board[row, cell.Col].Word.Equals(word))
					return false;
			}

			return true;
		}

		//returns 1 of 9 possible locations
		private RowColPair getRandomAdjacentCell(RowColPair cell)
		{
			int pos = Utils.RandomNumber(0, 9);
			switch (pos)
			{
				case 0:
					return new RowColPair(cell.Row - 1, cell.Col - 1);
				case 1:
					return new RowColPair(cell.Row - 1, cell.Col);
				case 2:
					return new RowColPair(cell.Row - 1, cell.Col + 1);
				case 3:
					return new RowColPair(cell.Row, cell.Col - 1);
				case 4:
					return new RowColPair(cell.Row, cell.Col);
				case 5:
					return new RowColPair(cell.Row, cell.Col + 1);
				case 6:
					return new RowColPair(cell.Row + 1, cell.Col - 1);
				case 7:
					return new RowColPair(cell.Row + 1, cell.Col);
				case 8:
					return new RowColPair(cell.Row + 1, cell.Col + 1);
				default:
					throw new Exception("Should not get here!");
			}
		}
	}
}
