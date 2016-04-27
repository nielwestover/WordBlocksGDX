using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LevelSolver
{
	public class Cell
	{
		public string c
		{
			get; set;
		}
		public int id { get; set; }
	}
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

	class Board
	{
		public List<int> solveOrder = new List<int>();
		public List<string> words;
		public RowColPair curCell;
		//public Dictionary<string, UniqueLetter> uniqueLetters;

		internal Board copy()
		{
			Board b = new Board(dim, dim);
			Array.Copy(board, b.board, board.Length);
			b.words = words.Clone().ToList();
			b.solveOrder = new List<int>(solveOrder);
			if (curCell == null)
				b.curCell = null;
			else
				b.curCell = new RowColPair(curCell.Row, curCell.Col);
			b.curWord = curWord;
			b.curLetterIndex = curLetterIndex;
			//b.uniqueLetters = uniqueLetters;
			return b;
		}

		public int dim;
		Cell[,] board;
		internal string curWord;
		internal int curLetterIndex;

		public Cell getCell(int row, int col)
		{
			if (row < 0 || row >= dim || col < 0 || col >= dim)
				throw new Exception("Row or Col are out of bounds: " + row + ", " + col);
			return board[row, col];
		}


		public Board(Level level)
		{
			dim = (int)Math.Sqrt((double)level.board.Count);

			words = level.words.Clone().ToList();
			board = new Cell[dim, dim];

			int index = 0;
			for (int row = 0; row < dim; ++row)
			{
				for (int col = 0; col < dim; ++col)
				{
					Cell c = new Cell();
					c.c = level.board[index].c;
					c.id = index;
					index++;
					board[row, col] = c;
				}
			}
			//computeUniqueLetters();
			//foreach (var item in uniqueLetters)
			//{
			//	if (item.Value == null)
			//	{
			//		Console.WriteLine("*************************************" + item.Key + "in level " + string.Join(" ", words.ToArray()) + "*********************************************"); 
			//	}
			//}
		}
		//public class UniqueLetter
		//{
		//	public int? id;
		//	public int uniqueIndex;
		//}
		//private void computeUniqueLetters()
		//{
		//	uniqueLetters = new Dictionary<string, UniqueLetter>();
		//	for (int i = 0; i < words.Count; ++i)
		//	{
		//		string curWord = words[i];
		//		uniqueLetters[curWord] = null;
		//		string uniqueChar = getUniqueCharacter(curWord);
		//		if (uniqueChar != "")
		//			uniqueLetters[curWord] = new UniqueLetter() { id = findIdOfUniqueCharacter(uniqueChar), uniqueIndex = curWord.IndexOf(uniqueChar) };
		//	}
		//}

		//private int? findIdOfUniqueCharacter(string uniqueChar)
		//{
		//	int? id = null;
		//	for (int row = 0; row < dim; ++row)
		//	{
		//		for (int col = 0; col < dim; ++col)
		//		{
		//			if (board[row, col].c.Equals(uniqueChar))
		//			{
		//				if (id == null)
		//					id = board[row, col].id;
		//				else
		//					return null;
		//			}
		//		}
		//	}
		//	return id;
		//}


		//private string getUniqueCharacter(string curWord)
		//{
		//	for (int i = 0; i < curWord.Length; i++)
		//	{
		//		char c = curWord[i];
		//		if (!charAppearsInOtherWords(c, curWord))
		//			return c.ToString();
		//		continue;
		//	}
		//	return "";
		//}

		//private bool charAppearsInOtherWords(char c, string curWord)
		//{
		//	foreach (string item in words)
		//	{
		//		if (curWord == item)
		//			continue;
		//		for (int i = 0; i < item.Length; ++i)
		//		{
		//			if (c == item[i])
		//				return true;
		//		}
		//	}
		//	return false;
		//}

		internal List<RowColPair> getLetterLocationsNearby(string candidate, RowColPair cell)
		{
			RowColPair uniqueLoc = null;
			//if (uniqueLetters[curWord] != null)
			//	getLocationByID(uniqueLetters[curWord].id);			
			List<RowColPair> locs = new List<RowColPair>();
			for (int row = -1; row < 2; ++row)
			{
				for (int col = -1; col < 2; ++col)
				{
					int candRow = cell.Row + row;
					int candCol = cell.Col + col;
					if (candRow >= 0 && candRow < dim &&
						candCol >= 0 && candCol < dim &&
						board[candRow, candCol] != null &&
						board[candRow, candCol].c.Equals(candidate) &&
						!solveOrder.Contains(board[candRow, candCol].id))
					{
						//if (uniqueLoc == null || closeEnoughToUniqueLetter(uniqueLoc, candRow, candCol))
							locs.Add(new RowColPair(candRow, candCol));
					}
				}
			}
			return locs;
		}

		internal List<RowColPair> getLetterLocations(string letter)
		{
			RowColPair uniqueLoc = null;
			//if (uniqueLetters[curWord] != null)
			//	getLocationByID(uniqueLetters[curWord].id);
			List<RowColPair> locs = new List<RowColPair>();
			for (int row = 0; row < dim; ++row)
			{
				for (int col = 0; col < dim; ++col)
				{
					if (board[row, col] != null && board[row, col].c.Equals(letter))
					{
						//if (uniqueLoc == null || closeEnoughToUniqueLetter(uniqueLoc, row, col))
						locs.Add(new RowColPair(row, col));
					}
				}
			}
			return locs;
		}

		//private bool closeEnoughToUniqueLetter(RowColPair uniqueLoc, int row, int col)
		//{
		//	//return true;
		//	//if (uniqueLetters[curWord] == null)
		//	//	return true;//N/A
		//	int letterDistance = Math.Abs(uniqueLetters[curWord].uniqueIndex - curLetterIndex);
		//	int rowDist = Math.Abs(uniqueLoc.Row - row);
		//	int colDist = Math.Abs(uniqueLoc.Col - col);
		//	if (letterDistance >= Math.Max(rowDist, colDist))
		//		return true;
		//	return false;
		//}

		//private RowColPair getClosestCharacterPosition(string ch, RowColPair startPos)
		//{
		//	int bestDistance = 100;
		//	RowColPair bestLoc = null;
		//	for (int row = 0; row < dim; ++row)
		//	{
		//		for (int col = 0; col < dim; ++col)
		//		{
		//			if (board[row, col] != null && board[row, col].c == ch)
		//			{
		//				RowColPair charPos = new RowColPair(row, col);
		//				int dist = getDistance(startPos, charPos);
		//				if (dist < bestDistance)
		//				{
		//					bestDistance = dist;
		//					bestLoc = charPos;
		//				}
		//			}

		//		}
		//	}
		//	return bestLoc;
		//}

		//private int getDistance(RowColPair pos1, RowColPair pos2)
		//{
		//	int rowDist = Math.Abs(pos1.Row - pos2.Row);
		//	int colDist = Math.Abs(pos1.Col - pos2.Col);
		//	return Math.Max(rowDist, colDist);
		//}

		//private RowColPair getLocationByID(int? v)
		//{
		//	if (v == null)
		//		return null;
		//	for (int row = 0; row < dim; ++row)
		//	{
		//		for (int col = 0; col < dim; ++col)
		//		{
		//			if (board[row, col] != null && board[row, col].id == v)
		//				return new RowColPair(row, col);
		//		}
		//	}
		//	return null;
		//}

		public Board(int rows, int cols)
		{
			board = new Cell[rows, cols];
			dim = rows;
		}

		public void dropWords()
		{
			for (int col = 0; col < dim; ++col)
			{
				for (int row = 0; row < dim - 1; ++row)
				{
					if (board[row, col] != null && solveOrder.Contains(board[row, col].id))
						board[row, col] = null;
				}
			}
			bool blockDropped = true;
			while (blockDropped)
			{
				blockDropped = false;
				for (int col = 0; col < dim; ++col)
				{
					for (int row = 0; row < dim - 1; ++row)
					{
						if (board[row, col] == null)
						{
							if (row + 1 < dim && board[row + 1, col] != null)
							{
								board[row, col] = board[row + 1, col];
								board[row + 1, col] = null;
								blockDropped = true;
							}
						}
					}
				}
			}
		}

		//public void print()
		//{
		//	//System.Console.WriteLine("******************************");
		//	foreach (var item in words)
		//	{
		//		System.Console.Write(item + " ");
		//	}
		//	System.Console.WriteLine();
		//	//row
		//	for (int row = dim - 1; row >= 0; --row)
		//	{
		//		//col
		//		for (int col = 0; col < dim; ++col)
		//		{
		//			if (board[row, col] != null)
		//				if (curCell != null && curCell.equals(row, col))
		//					System.Console.Write("*" + board[row, col].c + "*");
		//				else if (solveOrder.Contains(board[row, col].id))
		//					System.Console.Write("-" + board[row, col].c + "-");
		//				else
		//					System.Console.Write(" " + board[row, col].c + " ");
		//			else
		//				System.Console.Write(" . ");
		//		}
		//		System.Console.WriteLine();
		//	}
		//	System.Console.WriteLine();
		//}

	}
}
