package com.primary;

import java.util.*;

public class ReversePolishNotation {
	
	//method to check precedence
	public int precedence(String x) {
		int point=0;
		Map<String,Integer> prec = new HashMap<String, Integer>();
		prec.put("+",1);
		prec.put("-",1);
		prec.put("*",2);
		prec.put("/",2);
		prec.put("%",3);
		prec.put("POW",4);
		point=prec.get(x);
		return point;
	}
	//method to print postfix expression
	public void printExp(Queue<String> post) {
		System.out.print("Postfix Expression: ");
		while(!post.isEmpty()) {
			String curr=post.peek();
			if(curr.length()>1) {
				System.out.print(curr+" ");
			}
			else {
				System.out.print(curr);
			}
			post.remove();
		}
		System.out.println();
	}
	//method to evaluate output
	public double solve(Queue<String> postQueue) {
		Stack<Double> eval=new Stack<Double>();
		// postQ contains the postfix expression to be evaluated.
		String t;
		double topNum, nextNum, answer=0;
		while(!postQueue.isEmpty()) {
			t=postQueue.peek();
			postQueue.remove();
			if(t.matches("\\d+(\\.\\d+)*")) {
				eval.push(Double.parseDouble(t));
			}
			else {
				topNum=eval.peek();
				eval.pop();
				nextNum=eval.peek();
				eval.pop();
				
				switch(t) {
				case "+": answer=nextNum+topNum; break;
				case "-": answer=nextNum-topNum; break;
				case "*": answer=nextNum*topNum; break;
				case "/": answer=nextNum/topNum; break;
				case "%": answer=nextNum%topNum; break;
				case "POW": answer=Math.pow(nextNum, topNum); break;
				}
				eval.push(answer);
				//System.out.println("answer: "+answer);
			}
		}
		return eval.peek();
	}
	//method to convert infix to postfix
	public Queue<String> converter(Queue<String> infixQ) {
		Stack<String> opStack=new Stack<String>();
		Queue<String> postQ=new LinkedList<String>();
		// infixQ contains the infix expression to be converted.
		// postQ contains the converted postfix expression.
		String t;
		while (!infixQ.isEmpty()) {
			t = infixQ.peek(); 
			infixQ.remove();
			if (t.matches("\\d+(\\.\\d+)*")) {
				postQ.add(t);
			}
			else if (t.equals("(")) {
				opStack.add(t);
			}
			else if (t.equals(")")) { 
				while (!opStack.peek().equals("(")) {
					postQ.add(opStack.peek());
					opStack.pop();
				}
				opStack.pop(); // discard a left parenthesis from stack
			}
			else { 
				while ( !opStack.isEmpty() && !opStack.peek().equals("(") && precedence(t)<=precedence(opStack.peek())) {
					postQ.add(opStack.peek());
					opStack.pop();
				}
				opStack.add(t);
			}
		}
		// Now there are no tokens left in infixQ, so transfer remaining operators.
		while (!opStack.isEmpty()) { // (6)
			postQ.add(opStack.peek());
			opStack.pop();
		}
		//returning postfix expression
		return postQ;
	}
	
	public static void main(String[] args) {
	
		//initialize scanner
		Scanner input=new Scanner(System.in);
		//declaring variables
		Stack<String> rpnStack;
		Stack<String> reverseStack;
		String infixInput = new String();
		//initializing rpn queue
		Queue<String> rpnQueue;
		//declaring input validation flag
		boolean validInputFlag;
		int bracketOpenCount;
		int bracketCloseCount;
		
		while(!infixInput.equals("q")||!infixInput.equals("Q")) {
			validInputFlag=true;
			rpnStack= new Stack<>();
			reverseStack= new Stack<>();
			rpnQueue= new LinkedList<>();
			bracketOpenCount=0;
			bracketCloseCount=0;
			//input expression from user
			System.out.print("Enter the infix expression: ");
			infixInput=input.next();
			
			if(infixInput.equals("q")||infixInput.equals("Q")) {
				System.out.println("Thank You!");
				return;
			}
			//initializing class
			ReversePolishNotation rpn=new ReversePolishNotation();
			
			//iterating over entered string/expression
			for(int i=0;i<infixInput.length();i++) {
				//if queue is empty
				if(rpnStack.isEmpty()&&Character.isDigit(infixInput.charAt(i))) {
					rpnStack.add(String.valueOf(infixInput.charAt(i)));
				}
				//if head of queue is number and next item is number
				else if(Character.isDigit(rpnStack.peek().charAt(rpnStack.peek().length()-1))
						&&Character.isDigit(infixInput.charAt(i))) {
					String prev=rpnStack.peek();
					rpnStack.pop();
					prev=prev+String.valueOf(infixInput.charAt(i));
					rpnStack.add(prev);
				}
				//if head of queue is number and next item is floating point
				else if(Character.isDigit(rpnStack.peek().charAt(rpnStack.peek().length()-1))
						&&(infixInput.charAt(i)=='.')&&!rpnStack.peek().contains(".")) {
					String prev=rpnStack.peek();
					rpnStack.pop();
					prev=prev+String.valueOf(infixInput.charAt(i));
					rpnStack.add(prev);
				}
				//if head of queue is floating point and next item is digit
				else if((rpnStack.peek().charAt(rpnStack.peek().length()-1)=='.')&&Character.isDigit(infixInput.charAt(i))) {
					String prev=rpnStack.peek();
					rpnStack.pop();
					prev=prev+String.valueOf(infixInput.charAt(i));
					rpnStack.add(prev);
				}
				//if head of queue is digit and next item is allowed symbol
				else if(rpnStack.peek().matches("\\d+(\\.\\d+)*")&&
						(infixInput.charAt(i)=='+'||infixInput.charAt(i)=='-'||infixInput.charAt(i)=='*'||infixInput.charAt(i)=='%'
						||infixInput.charAt(i)=='/'||infixInput.charAt(i)==')')){
					rpnStack.add(String.valueOf(infixInput.charAt(i)));
					if(infixInput.charAt(i)==')') {
						bracketCloseCount++;
					}
				}
				//if head of queue is digit and next item is P
				else if(rpnStack.peek().matches("\\d+(\\.\\d+)*")&&infixInput.charAt(i)=='P'){
					rpnStack.add(String.valueOf(infixInput.charAt(i)));
				}
				//if head of queue is P and next item is O
				else if(rpnStack.peek().equals("P")&&infixInput.charAt(i)=='O'){
					String prev=rpnStack.peek();
					rpnStack.pop();
					prev=prev+String.valueOf(infixInput.charAt(i));
					rpnStack.add(prev);
				}
				//if head of queue is PO and next item is W
				else if(rpnStack.peek().equals("PO")&&infixInput.charAt(i)=='W'){
					String prev=rpnStack.peek();
					rpnStack.pop();
					prev=prev+String.valueOf(infixInput.charAt(i));
					rpnStack.add(prev);
				}
				//if head of queue is allowed symbol and next item is digit
				else if((rpnStack.peek().equals("+")||rpnStack.peek().equals("-")||rpnStack.peek().equals("%")
						||rpnStack.peek().equals("*")||rpnStack.peek().equals("/")||rpnStack.peek().equals("(")||rpnStack.peek().equals("POW"))
						&&Character.isDigit(infixInput.charAt(i))) {
					rpnStack.add(String.valueOf(infixInput.charAt(i)));
				}
				//if head of queue is allowed symbol and next item is bracket
				else if((rpnStack.peek().equals("+")||rpnStack.peek().equals("-")
						||rpnStack.peek().equals("*")||rpnStack.peek().equals("/")||rpnStack.peek().equals("%"))
						&&infixInput.charAt(i)=='(') {
					rpnStack.add(String.valueOf(infixInput.charAt(i)));
					bracketOpenCount++;
				}
				//if head of queue is bracket and next item is allowed symbol
				else if(rpnStack.peek().equals(")")&&(infixInput.charAt(i)=='+'||infixInput.charAt(i)=='%'
						||infixInput.charAt(i)=='-'||infixInput.charAt(i)=='*'||infixInput.charAt(i)=='/'||rpnStack.peek().equals("POW"))) {
					rpnStack.add(String.valueOf(infixInput.charAt(i)));
				}
				else {
					validInputFlag=false;
					break;
				}
			}
			
			if(bracketOpenCount!=bracketCloseCount) {
				validInputFlag=false;
			}
			if(validInputFlag) {
				//populating reverse stack
				while(!rpnStack.isEmpty()) {
					reverseStack.add(rpnStack.peek());
					rpnStack.pop();
				}
				//populating rpn queue
				while(!reverseStack.isEmpty()) {
					rpnQueue.add(reverseStack.peek());
					reverseStack.pop();
				}
				//converting from infix to postfix
				Queue<String> convertedExp=rpn.converter(rpnQueue);
				Queue<String> printQueue=new LinkedList<>();
				printQueue.addAll(convertedExp);
				
				//print postfix expression
				rpn.printExp(printQueue);
				
				//evaluating answer
				double ans=rpn.solve(convertedExp);
				
				System.out.println("Final Answer: "+(double)Math.round(ans*10000.0)/10000.0);
			}
			else {
				System.out.println("Invalid Input!");
			}
		}
		//close scanner
		input.close();
	}
}