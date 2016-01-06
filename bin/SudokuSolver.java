package bin;

import java.util.Scanner;
import java.io.IOException;

public class SudokuSolver {
		
	private int[][] matrixSudoku;
	private int[][] matrixTemp;
	public int rowEff, colEff;
	public int a, b, c, d;
	public int counter_exactSolving;
	public int nbElmt;
	int[] info_row = new int[82];
	int[] info_col = new int[82];
	int[][] info_value = new int[10][82];
	int[][][] hasValue = new int[10][10][10];
	
	/** Konstruktor **/
	public SudokuSolver(int[][] matrixOutput, int row, int col) {
		matrixSudoku = matrixOutput;
		matrixTemp = matrixOutput;
		rowEff = row;
		colEff = col;
		
		//Inisialisasi setiap kotak dengan angka 1-9
		for (a=0; a < rowEff; a++) {
			for (b=0; b < colEff; b++) {
				for (c=1; c <= 9; c++) {
					hasValue[c][a][b] = 1;
				}
            }
        }
	}
	
	int getSubRectRow(int row, int col) throws Exception {
		/* Mengembalikan nilai baris teratas dari sebuah kotak kecil */
		if (row >= 0 && row <= 2) {
			return 0;
		} else if (row >= 3 && row <= 5) {
			return 3;
		} else if (row >= 6 && row <= 8) {
			return 6;
		} else {
			return -1;
		}
	}
	
	int getSubRectCol(int row, int col) throws Exception {
		/* Mengembalikan nilai kolom terkiri dari sebuah kotak kecil */
		if (col >= 0 && col <= 2) {
			return 0;
		} else if (col >= 3 && col <= 5) {
			return 3;
		} else if (col >= 6 && col <= 8) {
			return 6;
		} else {
			return -1;
		}
	}
	
	void reInitializedSudoku() throws Exception {
		/* Me-reinisialisasi angka hasOne, hasTwo, hasThree, dst dimana tidak boleh ada angka yang berulang pada
		 * satu kolom dan baris dan kotak yang lebih kecil */
		 
		 int rowRect, colRect;
		 int a1, b1, c1, d1;
		 
		 for (a1=0; a1 < rowEff; a1++) {
			 for (b1=0; b1 < colEff; b1++) {
				 if (matrixSudoku[a1][b1] != 0) {
					 //Re-initialized untuk sebuah baris
					 for (c1=0; c1 < colEff; c1++) {
						hasValue[matrixSudoku[a1][b1]][a1][c1] = 0;
					 }
					 //Re-initialized untuk sebuah kolom
					 for (c1=0; c1 < rowEff; c1++) {
						 hasValue[matrixSudoku[a1][b1]][c1][b1] = 0;
					 }
					 //Re-initialized untuk kotak kecil
					 rowRect = getSubRectRow(a1, b1);
					 colRect = getSubRectCol(a1, b1);
					 for (c1=rowRect; c1 <= rowRect+2; c1++) {
						 for (d1=colRect; d1 <= colRect+2; d1++) {
							 hasValue[matrixSudoku[a1][b1]][c1][d1] = 0;
						 }
					 }
				 }
			 }
		 }
	}
	
	void exactSolving() throws Exception {
		/* Memberikan solusi untuk kotak yang sudah pasti nilainya (kondisi dimana pada sebuah baris / kolom
		   terdapat 1 buah kotak kosong) */
		   
		int counter, getrow, getcol;
		
		getrow = 0;
		getcol = 0;
		
		//exactSolving for row
		for (a=0; a < rowEff; a++) {
			counter = 0;
			for (b=0; b < colEff; b++) {
				if (matrixSudoku[a][b] == 0) {
					counter++;
					getrow = a;
					getcol = b;
				}
			}
			if (counter == 1) {
				for (c=1; c <= 9; c++) {
					if (hasValue[c][getrow][getcol] == 1) {
						matrixSudoku[getrow][getcol] = c;
					}
				}
				counter_exactSolving++;
				reInitializedSudoku();
			}
		}
		
		//exactSolving for column
		for (a=0; a < colEff; a++) {
			counter = 0;
			for (b=0; b < rowEff; b++) {
				if (matrixSudoku[b][a] == 0) {
					counter++;
					getrow = b;
					getcol = a;
				}
			}
			if (counter == 1) {
				for (c=1; c <= 9; c++) {
					if (hasValue[c][getrow][getcol] == 1) {
						matrixSudoku[getrow][getcol] = c;
					}
				}
				counter_exactSolving++;
				reInitializedSudoku();
			}
		}
		
		//exactSolving for sub rectangle
		int valrow = 0, valcol = 0, status = 0;
		
		while (status == 0) {
			
			if (valrow * valcol == 36) {
				status = 1;
			}
			
			counter = 0;
			for (a=valrow; a <= valrow+2; a++) {
				for (b=valcol; b <= valcol+2; b++) {
					if (matrixSudoku[a][b] == 0) {
						counter++;
						getrow = a;
						getcol = b;
					}
				}
			}
			if (counter == 1) {
				for (c=1; c <= 9; c++) {
					if (hasValue[c][getrow][getcol] == 1) {
						matrixSudoku[getrow][getcol] = c;
					}
				}
				counter_exactSolving++;
				reInitializedSudoku();
			}
		
			if (valcol != 6) {
				valcol = valcol+3;
			} else {
				valcol = 0;
				valrow = valrow + 3;
			}
		}
		
	}
	
	//Membentuk sebuah list untuk sel kosong
	void createList() throws Exception {
		int addr_counter = 0;
		
		for (a=0; a < rowEff; a++) {
			for (b=0; b < colEff; b++) {
				if (matrixSudoku[a][b] == 0) {
					//representasi alamat memori dengan index angka
					addr_counter++;
					//inisialisasi isi sebuah elemen list
					for (c=1; c <= 9; c++) {
						info_value[c][addr_counter] = 0;
					}
					//re-inisialisasi isi sebuah elemen list
					for (c=1; c <= 9; c++) {
						if (hasValue[c][a][b] == 1) {
							info_value[c][addr_counter] = 1; 
						}
					}
					//menentukan info untuk row dan column sebuah elemen list
					info_row[addr_counter] = a;
					info_col[addr_counter] = b;
				}
			}
		}
		
		nbElmt = addr_counter;
	}
	
	//Menghentikan program sampai user menekan tombol Enter
	void pauseProg(){
		System.out.println("Press enter to continue...");
		Scanner keyboard = new Scanner(System.in);
		keyboard.nextLine();
	}
	
	//Proses solving dengan algoritma brute force
	void solving() throws Exception {
		int addr_counter = 1;
		int found, found_possible_value;
		int getrow, getcol;
		
		while (addr_counter <= nbElmt) {
			
			found_possible_value = 0;
			
			for (c=1; c <= 9; c++) {
				if (info_value[c][addr_counter] == 1) {
					found = 0;
					//cek baris, apakah ada yang sama
					for (a=0; a < colEff; a++) {
						if (matrixTemp[info_row[addr_counter]][a] == c) {
							found = 1;
						}
					}
					//cek kolom, apakah ada yang sama
					for (a=0; a < rowEff; a++) {
						if (matrixTemp[a][info_col[addr_counter]] == c) {
							found = 1;
						}
					}
					//cek sub kotak, apakah ada yang sama
					getrow =  getSubRectRow(info_row[addr_counter], info_col[addr_counter]);
					getcol = getSubRectCol(info_row[addr_counter], info_col[addr_counter]);
					for (a=getrow; a <= getrow+2; a++) {
						for (b=getcol; b <= getcol+2; b++) {
							if (matrixTemp[a][b] == c) {
								found = 1;
							}
						}
					}
					
					if (found == 0) {
						//inisialisasi sel kosong yg bersangkutan dengan nilai c
						matrixTemp[info_row[addr_counter]][info_col[addr_counter]] = c;
						info_value[c][addr_counter] = -1;
						found_possible_value = 1;
						break;
					} else {
						//do nothing - mencari nilai c yang lain
					}
				}
			}
			
			//mencetak hasil Sudoku sementara
			/*
			System.out.println("matrixTemp:"+" "+info_row[addr_counter]+" "+info_col[addr_counter]);
			for (a=0; a < rowEff; a++) {
				for (b=0; b < colEff; b++) {
					System.out.print(matrixTemp[a][b]+" ");
				}
				System.out.println();
			}
			*/
			//pauseProg();
			
			if (found_possible_value == 1) {
				addr_counter++;
			} else {
				for (c=1; c <= 9; c++) {
					if (info_value[c][addr_counter] == -1) {
						info_value[c][addr_counter] = 1;
					}
				}
				matrixTemp[info_row[addr_counter]][info_col[addr_counter]] = 0;
				if (addr_counter - 1 >= 1) {
					addr_counter--;
					matrixTemp[info_row[addr_counter]][info_col[addr_counter]] = 0;
				} else {
					System.out.println("No solution");
					break;
				}
			}
		}
	}
	
	//Proses solving mulai dari sini
	void solver() throws Exception {
	
		reInitializedSudoku();			//me-reinisialisasi angka hasValue
		
		counter_exactSolving = 1;
		while (counter_exactSolving != 0) {
			counter_exactSolving = 0;
			exactSolving(); 			//mengisi kotak yang sudah pasti nilainya
			System.out.println("counter exact:"+" "+counter_exactSolving);
		}
		
		createList();					//membuat list bagi sel kosong
		
		matrixTemp = matrixSudoku;		//menyamakan matrix sementara dengan matrix asli
		
		solving();						//proses solving dengan algoritma brute force
		
		//print hasil akhir sudoku
		System.out.println();
		for (a=0; a<rowEff; a++) {
			for (b=0; b<colEff; b++) {
				System.out.print(matrixSudoku[a][b]+" ");
			}
			System.out.println();
		}
	}
	
}

/*
 * for (a=0; a < rowEff; a++) {
			for (b=0; b < colEff; b++) {
				if (matrixSudoku[a][b] == 0) {
				System.out.print(a+" "+b+":"+" ");
				for (c=1; c<=9; c++) {
					if (hasValue[c][a][b] == 1) {
						System.out.print(c+" ");
					}
				}
				System.out.println();
				}
			}
		}
*/
