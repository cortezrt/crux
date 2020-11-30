package Parser;

import Lexer.*;
import util.*;
import java.util.Hashtable;
import java.util.ArrayList;


public class Parser {
	ArrayList characters;
	ArrayList<lexeme> tokens;
	lexeme sym;
	int cnt;
	SSAgen out;
	Lexer L;
	private Hashtable<Integer, Integer> symbolTable;
	public Parser(String in) {
		cnt = 0;
		out  = new SSAgen();
		L = new Lexer(in);
		tokens = L.getTokens();
		sym = tokens.get(0);
		// associates an identifier ID with an Instruction ID value representing all values o
		symbolTable = new Hashtable<Integer, Integer>();
		}

	//declaration-list
	public void parse() throws Exception {
		declaration_list();
		// emit SSA
		System.out.println(symbolTable);
		out.emit();
	}
	public void declaration_list() throws Exception {
		out.add_block(0);
		while (!L.last_sym(sym)) {
			declaration();
		}
	}
	private void declaration() throws Exception  {
		switch (sym.getName()) {
			case "var":
				variable_declaration();
				break;
			case "func":
				func_definition();
				break;
			case "array":
				array_declaration();
				break;
			default:
				//System.out.println(sym.getName());
				error("Expected a declaration here!");
				break;
		}
	}
	private void updateST(Integer IdentifierID, int val) {
		symbolTable.put(IdentifierID, val);
	}
	private void variable_declaration() throws Exception {
		next();
		// place variable ID on symbol table
		symbolTable.put(sym.getID(), -1);
		next();
		next();
		next();
		next();
	}
	private void assignment_statement() {
		next();
		int identifierID = sym.getID();
		designator();
		next();
		Result val = expression0();
		int instructionID;
		// assigns identifierID to the RHS result (was -1)
		if (val.getKind() == Result.kind.Constant) {
			instructionID = out.addConstant(val);
		}
		else {
			instructionID = val.getResult();
		}
		updateST(identifierID, instructionID);
		next(); // consume ;
	}
	private void if_statement() throws Exception {
		next();
		expression0();
		statement_block();
		if (sym.getName().equals("else")) {
			statement_block();
		}
	}
	private void while_statement() throws Exception{
		next();
		expression0();
		statement_block();
	}
	private void return_statement() {
		next();
		expression0();
		next();
	}
	private void func_definition() throws Exception{
		if (!sym.getName().equals("func")) {
			error("invalid func");
		}
		next();
		// extract the func_id from sym, and consume IDENTIFIER
		int func_id = sym.getID();	// function identifier - used when calling functions
		next();
		consume_parameter_list();
		next(); // consume colon
		type(); // consume type;
		statement_block();
	}
	private void consume_parameter_list() throws Exception {
		if (!sym.getName().equals("(")) {
			System.out.println(sym.getName());
			error("expected an open paren at the start of consume parameter list");
		}
		next(); // consume open paren
		// something with parameters...
		next(); // consume close pararen
	}
	private void type() {
		next();
	}
	private void statement_block() throws Exception {
		next(); // consume curly brace
		statement_list();
		next(); // consume curly brace
	}
	private void statement_list() throws Exception{
		while (!sym.getName().equals("}")) {
			statement();
		}
	}
	private void statement() throws Exception {
		//do-stuff
		//System.out.println(sym.getName());
		switch (sym.getName()) {
			case "var":
				variable_declaration();
				break;
			case "::":
				call_statement();
				break;
			case "let":
				assignment_statement();
				break;
			case "if":
				if_statement();
				break;
			case "while":
				while_statement();
				break;
			case "return":
				return_statement();
				break;
			default:
				break;
		}
	}
	private void call_statement() {
		call_expression();
		next(); // consume ;
	}
	// placeholder - must implement user defined FNs
	private Result call_expression() {
		Result ans = new Result(); // default to null (only ret result if non-void func)
		next(); // consume ::
		int call_id = sym.getID();
		// op 4 = write 5 = read 6 = writeNL
		var funcName = sym.getName();
		next();
		next(); // consume IDENTIFIER and (
		ArrayList<Result> operands = call_expression_list();
		switch (funcName) {
			case "println":
				out.add_instruction(6,null,null);
				break;
			case "readint":
				int instr_id = out.add_instruction(5, null, null);
				ans = new Result(Result.kind.Variable, instr_id);
				break;
			case "printint":
				out.add_instruction(4, operands.get(0), null);
				break;	// print is a void fn - no need to return a valid result.
			default:	// implement user defined functions & printBool
				ans = new Result();
				break;
		}
		next(); // consume )
		return ans;
	}
	private ArrayList<Result> call_expression_list() {
		ArrayList<Result> ret = new ArrayList();
		if (sym.getName().equals(")")) {
			return ret;
		}
		ret.add(expression0());
		while (sym.getName().equals(",")) {
			next(); // consume comma
			ret.add(expression0());
		}
		return ret;
	}
	private void array_declaration() throws Exception {
		// emit some SSA code here...
	}
	// operators
	private int op0() {
		String token = sym.getName();
		switch(token) {
			case "<=":
				next();
				return 7;
			case ">=":
				next();
				return 8;
			case "!=":
				next();
				return 9;
			case "==":
				next();
				return 10;
			case ">":
				next();
				return 11;
			case "<":
				next();
				return 12;
			default:
				return -1;
		}
	}
	private int op1() {
		String token = sym.getName();
		switch (token) {
			case "+":
				next();
				return 0;
			case "-":
				next();
				return 1;
			case "or":
				next();
				return 8;
			default:
				//next();
				return -1;
		}
	}
	private int op2() {
		var token = sym.getName();
		switch (token) {
			case "*":
				next();
				return 2;
			case "/":
				next();
				return 3;
			case "and":
				next();
				return 9;
			default:
				return -1;
		}
	}
	// Numerical Expression parsing

	Result compute_result(Result left, Result right, int operator) {
		if (out.isCompileTimeOperation(left, right)) {
			// if both operands are constants, do the compile time operation and return as a constant
			return out.doCompileTimeOperation(operator, left, right);
		}
		else {
			// if either operand is a variable or register, emit SSA IR
			int temp = out.add_instruction(operator, left, right);
			Result.kind kind = Result.findReturnKind(operator, left, right);
			return new Result(kind, temp);
		}
	}
	Result expression0(){
		Result left = expression1();
		int op = op0();
		if ( op != -1) {
			Result right = expression1();
			left = compute_result(left, right, op);
		}
		return left;
	}
	Result expression1() {
		Result left = expression2();
		int op = op1();
		while (op != -1) {
			var right = expression2();
			left = compute_result(left, right, op);
			op = op1();
		}
		return left;
	}
	Result expression2() {
		Result left = expression3();
		int op = op2();
		while (op != -1) {
			var right = expression3();
			left = compute_result(left, right, op);
			op = op2();
		}
		return left;
	}
	Result expression3() {
		Result left;
		switch (sym.getName()) {
			case "not":
				// add a case in compute_result for the "not" case (opcode 200)
				// ... ignoring for now b/c booleans not tested
				next();
				left = expression3();
				//left.negate();
				break;
			case "(":
				next();
				left = expression0();
				next();
				break;
			case "::":
				return call_expression();
			default:
				if (sym.isIdentifier()) {
					left = designator();
					}
				else {
					left = literal();
				}
				break;
		}
		return left;
	}
	Result designator() {
		String name = sym.getName();
		int identifierID = sym.getID();
		next();
		// arrays and results are mean mean things that you must fix
		while (sym.getName().equals("[")) {
			next();
			expression0();
			next();
		}
		// must map
 		int IID =  symbolTable.get(identifierID);
		return new Result(Result.kind.Variable, IID);
	}
	Result literal() {
		String token = sym.getName();
		next();
		switch(token.toLowerCase()) {
			case "true":
				return new Result(Result.kind.Constant, 1);
			case "false":
				return new Result(Result.kind.Constant, 0);
			default:
				return new Result(Result.kind.Constant, Integer.parseInt(token));
		}
	}

	void next() {
		cnt++;
		if (cnt == tokens.size())
			return;
		sym = tokens.get(cnt);
	}
	void error(String msg) throws Exception {
		throw new Exception(msg);
	}
}

