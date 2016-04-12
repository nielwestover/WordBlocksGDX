using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace LevelGenerator
{
	public class Pair<T, U>
	{
		public Pair() { }
		public Pair(T first, U second)
		{
			this.Row = first;
			this.Col = second;
		}

		public T Row { get; set; }
		public U Col { get; set; }

		public override string ToString()
		{
			return Row + ", " + Col;
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
			return true;
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
				//cow
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
			Pair<int, int> cell = new Pair<int, int>(Utils.RandomNumber(0, dim), Utils.RandomNumber(0, dim));			
			//we're building the level from the final state to the initial state, so iterate over the letters in reverse order
			for (int i = word.Length - 1; i >= 0; --i)
			{
				cell = getNextCell(cell, word);
				//Got into a bad state, so bail and start over!
				if (cell == null)
					return false;
				insertCharacter(word[i], word, cell);
				//print();
			}
			return true;
		}

		//private void dropLetters()
		//{
		//	bool changed = true;
		//	while (changed)
		//	{
		//		changed = false;
		//		for (int col = 0; col < dim; ++col)
		//		{
		//			int curRow = dim - 2;
		//			while (curRow >= 0)
		//			{
		//				if (board[curRow, col] == null && board[curRow + 1, col] != null)
		//				{
		//					changed = true;
		//					board[curRow, col] = board[curRow + 1, col];
		//					board[curRow + 1, col] = null;
		//				}
		//				--curRow;

		//			}
		//		}
		//	}
		//}
		int hintIndex;

		private void insertCharacter(char ch, string word, Pair<int, int> cell)
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

		private Pair<int, int> getNextCell(Pair<int, int> cell, string word)
		{
			int count = 0;
			while (++count < 50)
			{
				Pair<int, int> newCell = getRandomAdjacentCell(cell);
				if (cellIsValid(newCell, word))
					return newCell;
			}
			return null;
		}

		private bool cellIsValid(Pair<int, int> cell, string word)
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
		private Pair<int, int> getRandomAdjacentCell(Pair<int, int> cell)
		{
			int pos = Utils.RandomNumber(0, 9);
			switch (pos)
			{
				case 0:
					return new Pair<int, int>(cell.Row - 1, cell.Col - 1);
				case 1:
					return new Pair<int, int>(cell.Row - 1, cell.Col);
				case 2:
					return new Pair<int, int>(cell.Row - 1, cell.Col + 1);
				case 3:
					return new Pair<int, int>(cell.Row, cell.Col - 1);
				case 4:
					return new Pair<int, int>(cell.Row, cell.Col);
				case 5:
					return new Pair<int, int>(cell.Row, cell.Col + 1);
				case 6:
					return new Pair<int, int>(cell.Row + 1, cell.Col - 1);
				case 7:
					return new Pair<int, int>(cell.Row + 1, cell.Col);
				case 8:
					return new Pair<int, int>(cell.Row + 1, cell.Col + 1);
				default:
					throw new Exception("Should not get here!");
			}
		}
	}
}
