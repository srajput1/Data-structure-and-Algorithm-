package primary;

import java.util.*;
import java.io.*;

public class TicTacToe {

	//non static variables
	private int gridCount;
	private int userCount;
	private int winSequenceCount;
	private int movePlayer;
	private int totalMoves;
	private int totalMovesPlayed;
	private String moveGrid[][];
	private ArrayList<String> userCode;
	
	//constructor for new game
	TicTacToe(int gridCount){
		moveGrid= new String[gridCount][gridCount];
		this.gridCount=gridCount;
		userCode= new ArrayList<>(Arrays.asList("X","O","A","B","C","D","E","F","G","H","I","J","K",
				"L","M","N","P","Q","R","S","T","U","V","W","Y","Z"));
	}
	//constructor for saved game
	TicTacToe(String fileName){
		userCode= new ArrayList<>(Arrays.asList("X","O","A","B","C","D","E","F","G","H","I","J","K",
				"L","M","N","P","Q","R","S","T","U","V","W","Y","Z"));
		try {
			BufferedReader brd=new BufferedReader(new FileReader(fileName));
			String inputLine=new String();
			try {
				//reading grid count
				inputLine=brd.readLine();
				gridCount=Integer.parseInt(inputLine);
				totalMoves=gridCount*gridCount;
				moveGrid= new String[gridCount][gridCount];
				//reading grid
				for(int i=0;i<gridCount;i++) {
					inputLine=brd.readLine();
					ArrayList<String> items= new ArrayList<String>(Arrays.asList(inputLine.split(",")));
					for(int j=0;j<gridCount;j++) {
						if(!items.get(j).equals(" ")) {
							moveGrid[i][j]=items.get(j);
						}
					}
				}
				//reading next move player number
				inputLine=brd.readLine();
				movePlayer=Integer.parseInt(inputLine);
				//reading player count
				inputLine=brd.readLine();
				userCount=Integer.parseInt(inputLine);
				//reading win sequence count
				inputLine=brd.readLine();
				winSequenceCount=Integer.parseInt(inputLine);
				//reading win sequence count
				inputLine=brd.readLine();
				totalMovesPlayed=Integer.parseInt(inputLine);
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(brd!=null) {
						brd.close();
					}
				}catch(IOException exc) {
					exc.printStackTrace();
				}
			}
		}catch(FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("No such file exists.");
		}
	}
	//method to save game
	private void saveGame(int playerId, int movePlayedCount) {
		
		BufferedWriter bw=null;
		//initialize scanner
		Scanner input=new Scanner(System.in);
		//take input of file name from user
		System.out.print("Enter the name of the file: ");  
		String name=input.nextLine();
		try {
	        bw = new BufferedWriter(new FileWriter(name));
	        //writing row limit to file
	        bw.write(Integer.toString(gridCount));
	        bw.newLine();
	        //writing of grid into file
	        for (int i = 0; i < gridCount; i++) {
	            for (int j = 0; j < gridCount; j++) {
	            	if(j<gridCount-1&&moveGrid[i][j]!=null) {
	            		bw.write(moveGrid[i][j] + ",");
	            	}
	            	else if(j==gridCount-1&&moveGrid[i][j]!=null){
	            		bw.write(moveGrid[i][j]);
	            	}
	            	else if(j<gridCount-1&&moveGrid[i][j]==null) {
	            		bw.write(" ,");
	            	}
	            	else {
	            		bw.write(" ");
	            	}
	            }
	            bw.newLine();
	        }
	        //writing player id
	        bw.write(Integer.toString(playerId));
	        bw.newLine();
	        //writing player count
	        bw.write(Integer.toString(userCount));
	        bw.newLine();
	        //writing win sequence count
	        bw.write(Integer.toString(winSequenceCount));
	        bw.newLine();
	        //writing played move count
	        bw.write(Integer.toString(movePlayedCount));
	        bw.newLine();
	        bw.flush();
	        System.out.println("Your game has been saved. Load it next time to resume.");
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		try {
			if (bw!= null)
				bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//method to start game
	private void startGame(int play,int movesPlayed) {
		//initialize game variables
		Boolean gameState=true;
		int movePlayer=play;
		int played=movesPlayed;
		//initialize scanner
		Scanner input=new Scanner(System.in);
		
		//game play starts
		while(gameState==true) {
			//Input file name from user
			System.out.print("Enter the move (row column) for Player "+movePlayer+" or Q to save and quit: ");  
			String move=input.nextLine();
			if((move.equals("Q"))||(move.equals("q"))) {
				saveGame(movePlayer,played);
				gameState=false;
				break;
			}
			else {
				if(move.matches("^[1-9]{1,3}[ ][1-9]{1,3}$")) {
					ArrayList<String> coordinates= new ArrayList<String>(Arrays.asList(move.split("\\s+")));
					int row=Integer.parseInt(coordinates.get(0));
					int column=Integer.parseInt(coordinates.get(1));
					if(row<=gridCount&&column<=gridCount) {
						if(moveGrid[row-1][column-1]==null) {
							moveGrid[row-1][column-1]=userCode.get(movePlayer-1);
							movesPlayed++;
							printGrid();
							if(movesPlayed==totalMoves) {
								System.out.println("Game is a draw");
								break;
							}
							boolean check=checkWin(row-1,column-1,userCode.get(movePlayer-1));
							if(check==true) {
								System.out.println("Congratulations! Player "+userCode.get(movePlayer-1)+" has won! End of Game. ");
								gameState=false;
								break;
							}
						}
						else {
							System.out.println("Another player has already placed a move in this postion. Please choose an empty space.");
							movePlayer--;
						}
					}
					else {
						System.out.println("Incorrect Input.");
						movePlayer--;
					}
				}
				else {
					System.out.println("Incorrect Input.");
					movePlayer--;
				}
			}
			
			//incrementing or resetting player number
			if(movePlayer==userCount) {
				movePlayer=1;
			}
			else {
				movePlayer++;
			}
		}
		//close scanner
		input.close();
	}
	//method to check win/lose/draw
	private boolean checkWin(int row, int column, String userSign){
		int counter=1;
		int checkRow=row;
		int checkColumn=column;
		boolean winState=false;
		boolean checkStatus=true;
			
		//check for row right
		while((checkColumn<gridCount-1)&&(checkStatus==true)){
			checkColumn++;
			if(moveGrid[checkRow][checkColumn]==null) {
				checkColumn--;
				break;
			}
			if(moveGrid[checkRow][checkColumn].equals(userSign)) {
				counter++;
				//checking for win
				if(counter==winSequenceCount) {
					winState=true;
					checkStatus=false;
					break;
				}
			}
			else {
				checkColumn--;
				break;
			}
		} 
		//resetting counter
		counter=1;
		//check for row left
		while((checkColumn>0)&&(checkStatus==true)){
			checkColumn--;
			if(moveGrid[checkRow][checkColumn]==null) {
				checkColumn=column;
				break;
			}
			if(moveGrid[checkRow][checkColumn].equals(userSign)) {
				counter++;
				//checking for win
				if(counter==winSequenceCount) {
					winState=true;
					checkStatus=false;
					break;
				}
			}
			else {
				checkColumn=column;
				break;
			}
		}
		//resetting counter
		counter=1;
		//check for column down
		while((checkRow<gridCount-1)&&(checkStatus==true)){
			checkRow++;
			if(moveGrid[checkRow][checkColumn]==null) {
				checkRow--;
				break;
			}
			if(moveGrid[checkRow][checkColumn].equals(userSign)) {
				counter++;
				//checking for win
				if(counter==winSequenceCount) {
					winState=true;
					checkStatus=false;
					break;
				}
			}
			else {
				checkRow--;
				break;
			}
		} 
		//resetting counter
		counter=1;
		//check for column up
		while((checkRow>0)&&(checkStatus==true)){
			checkRow--;
			if(moveGrid[checkRow][checkColumn]==null) {
				checkRow=row;
				break;
			}
			if(moveGrid[checkRow][checkColumn].equals(userSign)) {
				counter++;
				//checking for win
				if(counter==winSequenceCount) {
					winState=true;
					checkStatus=false;
					break;
				}
			}
			else {
				checkRow=row;
				break;
			}
		}
		//resetting counter
		counter=1;
		//check for primary diagonal down
		while((checkRow<gridCount-1)&&(checkColumn<gridCount-1)&&(checkStatus==true)){
			checkRow++;
			checkColumn++;
			if(moveGrid[checkRow][checkColumn]==null) {
				checkRow--;
				checkColumn--;
				break;
			}
			if(moveGrid[checkRow][checkColumn].equals(userSign)) {
				counter++;
				//checking for win
				if(counter==winSequenceCount) {
					winState=true;
					checkStatus=false;
					break;
				}
			}
			else {
				checkRow--;
				checkColumn--;
				break;
			}
		}
		//resetting counter
		counter=1;		
		//check for primary diagonal up
		while((checkRow>0)&&(checkColumn>0)&&(checkStatus==true)){
			checkRow--;
			checkColumn--;
			if(moveGrid[checkRow][checkColumn]==null) {
				checkRow=row;
				checkColumn=column;
				break;
			}
			if(moveGrid[checkRow][checkColumn].equals(userSign)) {
				counter++;
				//checking for win
				if(counter==winSequenceCount) {
					winState=true;
					checkStatus=false;
					break;
				}
			}
			else {
				checkRow=row;
				checkColumn=column;
				break;
			}
		}
		//resetting counter
		counter=1;
		//check for alternate diagonal down
		while((checkRow<gridCount-1)&&(checkColumn>0)&&(checkStatus==true)){
			checkRow++;
			checkColumn--;
			if(moveGrid[checkRow][checkColumn]==null) {
				checkRow--;
				checkColumn++;
				break;
			}
			if(moveGrid[checkRow][checkColumn].equals(userSign)) {
				counter++;
				//checking for win
				if(counter==winSequenceCount) {
					winState=true;
					checkStatus=false;
					break;
				}
			}
			else {
				checkRow--;
				checkColumn++;
				break;
			}
		}
		//resetting counter
		counter=1;		
		//check for alternate diagonal up
		while((checkRow>0)&&(checkColumn<gridCount-1)&&(checkStatus==true)){
			checkRow--;
			checkColumn++;
			if(moveGrid[checkRow][checkColumn]==null) {
				checkRow=row;
				checkColumn=column;
				break;
			}
			if(moveGrid[checkRow][checkColumn].equals(userSign)) {
				counter++;
				//checking for win
				if(counter==winSequenceCount) {
					winState=true;
					checkStatus=false;
					break;
				}
			}
			else {
				checkRow=row;
				checkColumn=column;
				break;
			}
		}
		return winState;
	}
	//method to print grid header
	private void printGridHeader(int row) {
		if(String.valueOf(row+1).length()==1) { //--> single digit rows
			System.out.print(" "+(row+1)+" ");
			System.out.print(" ");
		}
		else if(String.valueOf(row+1).length()==2) { //--> double digit rows
			System.out.print(" "+(row+1)+"");
			System.out.print(" ");
		}
		else if(String.valueOf(row+1).length()==3) { //--> triple digit rows
			System.out.print(""+(row+1)+" ");
		}
	}
	//method to print the tic tac toe grid
	private void printGrid() {
		//print tic-tac-toe grid
		for(int i=0;i<=gridCount;i++) {
			
			if(i==0) { //--> printing grid header
				if((gridCount>9)&&(gridCount<99)) {
					System.out.print("  ");
				}
				else if(gridCount>99) {
					System.out.print("   ");
				}
				else {
					System.out.print(" ");
				}
				for(int j=0;j<=gridCount;j++) {
					//printing column indexes
					if(j<gridCount) {
						printGridHeader(j);
					}
					else {
						System.out.print("\n");
					}
				}
			}
			else { // --> printing grid
				//printing row indexes
				if((gridCount>9)&&(gridCount<99)&&(String.valueOf(i).length()==1)) {
					System.out.print(" "+i);
				}
				else if((gridCount>99)&&(String.valueOf(i).length()==1)) {
					System.out.print("  "+i);
				}
				else if((gridCount>99)&&(String.valueOf(i).length()==2)) {
					System.out.print(" "+i);
				}
				else {
					System.out.print(i);
				}
				//printing rows
				for(int j=1;j<=gridCount;j++) {
					//check if move available for coordinate
					if(moveGrid[i-1][j-1]==null) { //--> blank space
						if(j!=gridCount) {
							System.out.print("   |");
						}
						else {
							System.out.print("\n");
						}
					}
					else { //-> player move block
						if(j!=gridCount) {
							System.out.print(" "+moveGrid[i-1][j-1]+" |");
						}
						else {
							System.out.print(" "+moveGrid[i-1][j-1]+" \n");
						}
					}
				}
				//printing row divider
				if(i!=gridCount) {
					for(int j=0;j<=gridCount;j++) {
						if(j==0) {
							if(gridCount<9){
								System.out.print(" ");
							}
							else if((gridCount>9)&&(gridCount<100)) {
								System.out.print("  ");
							}
							else {
								System.out.print("   ");
							}
							
						}
						else if(j!=gridCount) {
							System.out.print("---+");
						}
						else {
							System.out.print("---");
						}
					}
					System.out.print("\n");
				}
			}
		}
	}
	//main method
	public static void main(String[] args) {
		//initialize variables
		String userGameChoice=new String();
		String savedFile;
		int gridCount;
		TicTacToe obj;
		
		//initialize scanner
		Scanner input=new Scanner(System.in);
		
		while(!userGameChoice.equals("N")&&!userGameChoice.equals("n")&&!userGameChoice.equals("L")
				&&!userGameChoice.equals("l")&&!userGameChoice.equals("q")&&!userGameChoice.equals("q")) {
			//Option of game for user
			System.out.print("Enter N/n(new game),L/l(load game) or Q/q(quit): ");
			userGameChoice=input.nextLine();
			
			if(userGameChoice.equals("N")||userGameChoice.equals("n")) {
				boolean inputCheck=true;
				while(inputCheck) {
					//Input row/column from user
					do {
						System.out.print("Enter the number of rows/columns(3-999): ");
						String rawInput=input.next();
						while(!rawInput.matches("^[1-9]{1,3}$")||(Integer.parseInt(rawInput)<3||Integer.parseInt(rawInput)>999)) {
							System.out.print("Invalid input.\nEnter grid dimension (3-999): ");
							rawInput = input.next();
						}
						gridCount=Integer.parseInt(rawInput);
						break;
					}
					while(true);
					//initialize constructor
					obj=new TicTacToe(gridCount);
					//input number of users
					do {
						System.out.print("Enter the number of players(<=26): "); 
						String rawInput=input.next();
						while(!rawInput.matches("^[1-9]{1,2}$")||(Integer.parseInt(rawInput)>26)||(Integer.parseInt(rawInput)<2)){
							System.out.print("Invalid input.\nEnter the number of players: ");
							rawInput = input.next();
						}
						obj.userCount=Integer.parseInt(rawInput);
						break;
					}
					while(true);
					//Input win sequence count from user
					do {
						System.out.print("Enter the win sequence count(>2): ");  
						String rawInput=input.next();
						while(!rawInput.matches("^[1-9]{1,3}$")||(Integer.parseInt(rawInput)<=2)){
							System.out.print("Invalid input.\nEnter the win sequence count: ");
							rawInput = input.next();
						}
						obj.winSequenceCount=Integer.parseInt(rawInput);
						break;
					}
					while(true);
					
					//calculate total moves
					obj.totalMoves=gridCount*gridCount;
					
					if(obj.userCount<=gridCount&&obj.winSequenceCount<=gridCount&&obj.userCount<=gridCount&&obj.userCount>1) {
						//initialise grid
						obj.printGrid();
						//start game
						obj.startGame(1,0);
						inputCheck=false;
					}
					else {
						System.out.println("Invalid combination of grid limit, player count and win sequence count. Please enter again.");  
					}
					
				}
			}
			else if(userGameChoice.equals("L")||userGameChoice.equals("l")) {
				//input file name
				System.out.print("Enter fileName: ");
				savedFile=input.nextLine();
				//initialize constructor
				obj=new TicTacToe(savedFile);
				//print grid state
				obj.printGrid();
				//resume game
				obj.startGame(obj.movePlayer,obj.totalMovesPlayed);
			}
			else if(userGameChoice.equals("Q")||userGameChoice.equals("q")) {
				//quit
				System.out.println("Thank You.");
			}
			else {
				//invalid input message
				System.out.println("Invalid input.");  
			}
		}
		
		//close scanner
		input.close();
	}
}