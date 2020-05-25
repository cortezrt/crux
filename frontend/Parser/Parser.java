package frontend.Parser;

import frontend.Lexer.Lexer;
import frontend.SSA.SSAgen;

import java.util.ArrayList;

public class Parser {
	ArrayList characters;
	ArrayList<Lexer.lexeme> tokens;
	Lexer.lexeme sym;
	int cnt;
	SSAgen out;
	public Parser(String in) {
		cnt = 0;
		out  = new SSAgen();
		Lexer L = new Lexer(in);
		tokens = L.getTokens();
		sym = tokens.get(0);
		}

	public void parse() throws Exception {
		while (!sym.last_sym()) {
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
				error("Expected a declaration here!");
				break;
		}
	}
	private void variable_declaration() throws Exception {
		next();
		if (sym.isIdentifier())
			next();
		next();
		call_expression_list();
		next();
	}
	private void assignment_statement() {
		next();
		designator();
		next();
		expression0();
		next();
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
		if (sym.getName().equals("func")) {
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
		if (sym.getName().equals("(")) {
			error("expected an open paren at the start of consume parameter list");
		}
		next(); // consume open paren
		// something with parameters...
		next(); // consume close pararen
	}
	private void type() {
		next();
	}
	private void statement_block() throws Exception{
		next(); // consume curly brace
		statement_list();
		next(); // consume curly brace
	}
	private void statement_list() throws Exception{
		while (sym.getName() != "}") {
			statement();
		}
	}
	private void statement() throws Exception {
		//do-stuff
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
	}
	private void call_expression() {
		next(); // consume ::
		int call_id = sym.getID();
		next();
		next(); // consume IDENTIFIER and (
		call_expression_list();
		next(); // consume )

	}
	private void call_expression_list() {
		if (sym.getName().equals(")")) {
			return;
		}
		expression0();
		while (sym.getName().equals(",")) {
			next(); // consume comma
			expression0();
		}

	}
	private void array_declaration() throws Exception {
		// emit some SSA code here...
	}
	// operators
	private boolean op0()
	{
		String token = sym.getName();
		return token.matches(">=|<=|!=|==|>=|<");
	}
	private boolean op1() {
		String token = sym.getName();
		return token.matches("+|-|or");
	}
	private boolean op2() {
		return sym.getName().matches("*|/|and");
	}
	// Numerical Expression parsing
	void expression0(){
		expression1();
		next(); // move to next token
		if (op0()) {
			expression1();
		}
	}
	void expression1() {
		expression2();
		next();
		while (op1()) {
			expression2();
		}
	}
	void expression2() {
		expression3();
		next();
		while (op2()) {
			expression3();
		}
	}
	void expression3() {
		switch (sym.getName()) {
			case "not":
				next();
				expression3();
				break;
			case "(":
				next();
				expression0();
				next();
				break;
			case "::":
				call_expression();
			default:
				if (sym.isIdentifier()) {
					designator();
				}
				else {
					literal();
				}
				break;
		}
	}
	void designator() {
		if (sym.isIdentifier())
			next();
		while (sym.getName().equals("[")) {
			expression0();
			next();
		}
	}
	void literal() {
		next();
	}

	void next() {
		cnt++;
		sym = tokens.get(cnt);
	}
	void error(String msg) throws Exception {
		throw new Exception(msg);
	}
}

