/*******************************************************************************
 * This file is part of the Polyglot extensible compiler framework.
 *
 * Copyright (c) 2000-2008 Polyglot project group, Cornell University
 * Copyright (c) 2006-2008 IBM Corporation
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This program and the accompanying materials are made available under
 * the terms of the Lesser GNU Public License v2.0 which accompanies this
 * distribution.
 * 
 * The development of the Polyglot project has been supported by a
 * number of funding sources, including DARPA Contract F30602-99-1-0533,
 * monitored by USAF Rome Laboratory, ONR Grant N00014-01-1-0968, NSF
 * Grants CNS-0208642, CNS-0430161, and CCF-0133302, an Alfred P. Sloan
 * Research Fellowship, and an Intel Research Ph.D. Fellowship.
 *
 * See README for contributors.
 ******************************************************************************/

package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.Type;
import polyglot.types.Type;
import polyglot.types.Package;
import polyglot.util.Position;
import java.util.List;

/**
 * A <code>NodeFactory</code> constructs AST nodes. All node construction should
 * go through this factory or be done with the <code>copy()</code> method of
 * <code>Node</code>.
 */
public interface NodeFactory {

	/**
	 * Returns a disambiguator for nodes from this factory.
	 */
	Disamb disamb();

	// ////////////////////////////////////////////////////////////////
	// Factory Methods
	// ////////////////////////////////////////////////////////////////

	Id Id(Position pos, String id);

	AmbExpr AmbExpr(Position pos, Id name);

	/** @deprecated */
	AmbExpr AmbExpr(Position pos, String name);

	Expr ExprFromQualifiedName(Position pos, String qualifiedName);

	// type or expr
	AmbReceiver AmbReceiver(Position pos, Id name);

	AmbReceiver AmbReceiver(Position pos, Prefix prefix, Id name);

	/** @deprecated */
	AmbReceiver AmbReceiver(Position pos, String name);

	/** @deprecated */
	AmbReceiver AmbReceiver(Position pos, Prefix prefix, String name);

	Receiver ReceiverFromQualifiedName(Position pos, String qualifiedName);

	// package or type
	AmbQualifierNode AmbQualifierNode(Position pos, Id name);

	AmbQualifierNode AmbQualifierNode(Position pos, QualifierNode qual, Id name);

	/** @deprecated */
	AmbQualifierNode AmbQualifierNode(Position pos, String name);

	/** @deprecated */
	AmbQualifierNode AmbQualifierNode(Position pos, QualifierNode qual,
			String name);

	QualifierNode QualifierNodeFromQualifiedName(Position pos,
			String qualifiedName);

	// package or type or expr
	AmbPrefix AmbPrefix(Position pos, Id name);

	AmbPrefix AmbPrefix(Position pos, Prefix prefix, Id name);

	/** @deprecated */
	AmbPrefix AmbPrefix(Position pos, String name);

	/** @deprecated */
	AmbPrefix AmbPrefix(Position pos, Prefix prefix, String name);

	Prefix PrefixFromQualifiedName(Position pos, String qualifiedName);

	AmbTypeNode AmbTypeNode(Position pos, Id name);

	AmbTypeNode AmbTypeNode(Position pos, QualifierNode qualifier, Id name);

	/** @deprecated */
	AmbTypeNode AmbTypeNode(Position pos, String name);

	/** @deprecated */
	AmbTypeNode AmbTypeNode(Position pos, QualifierNode qualifier, String name);

	TypeNode TypeNodeFromQualifiedName(Position pos, String qualifiedName);

	ArrayTypeNode ArrayTypeNode(Position pos, TypeNode base);

	CanonicalTypeNode CanonicalTypeNode(Position pos, Type type);

	ArrayAccess ArrayAccess(Position pos, Expr base, Expr index);

	ArrayInit ArrayInit(Position pos);

	ArrayInit ArrayInit(Position pos, List elements);

	Assert Assert(Position pos, Expr cond);

	Assert Assert(Position pos, Expr cond, Expr errorMessage);

	Assign Assign(Position pos, Expr target, Assign.Operator op, Expr source);

	LocalAssign LocalAssign(Position pos, Local target, Assign.Operator op,
			Expr source);

	FieldAssign FieldAssign(Position pos, Field target, Assign.Operator op,
			Expr source);

	ArrayAccessAssign ArrayAccessAssign(Position pos, ArrayAccess target,
			Assign.Operator op, Expr source);

	AmbAssign AmbAssign(Position pos, Expr target, Assign.Operator op,
			Expr source);

	Binary Binary(Position pos, Expr left, Binary.Operator op, Expr right);

	Block Block(Position pos);

	Block Block(Position pos, Stmt s1);

	Block Block(Position pos, Stmt s1, Stmt s2);

	Block Block(Position pos, Stmt s1, Stmt s2, Stmt s3);

	Block Block(Position pos, Stmt s1, Stmt s2, Stmt s3, Stmt s4);

	Block Block(Position pos, List statements);

	SwitchBlock SwitchBlock(Position pos, List statements);

	BooleanLit BooleanLit(Position pos, boolean value);

	Branch Break(Position pos);

	Branch Break(Position pos, Id label);

	/** @deprecated */
	Branch Break(Position pos, String label);

	Branch Continue(Position pos);

	Branch Continue(Position pos, Id label);

	/** @deprecated */
	Branch Continue(Position pos, String label);

	Branch Branch(Position pos, Branch.Kind kind);

	Branch Branch(Position pos, Branch.Kind kind, Id label);

	/** @deprecated */
	Branch Branch(Position pos, Branch.Kind kind, String label);

	Call Call(Position pos, Id name);

	Call Call(Position pos, Id name, Expr a1);

	Call Call(Position pos, Id name, Expr a1, Expr a2);

	Call Call(Position pos, Id name, Expr a1, Expr a2, Expr a3);

	Call Call(Position pos, Id name, Expr a1, Expr a2, Expr a3, Expr a4);

	Call Call(Position pos, Id name, List args);

	Call Call(Position pos, Receiver target, Id name);

	Call Call(Position pos, Receiver target, Id name, Expr a1);

	Call Call(Position pos, Receiver target, Id name, Expr a1, Expr a2);

	Call Call(Position pos, Receiver target, Id name, Expr a1, Expr a2, Expr a3);

	Call Call(Position pos, Receiver target, Id name, Expr a1, Expr a2,
			Expr a3, Expr a4);

	Call Call(Position pos, Receiver target, Id name, List args);

	/** @deprecated */
	Call Call(Position pos, String name);

	/** @deprecated */
	Call Call(Position pos, String name, Expr a1);

	/** @deprecated */
	Call Call(Position pos, String name, Expr a1, Expr a2);

	/** @deprecated */
	Call Call(Position pos, String name, Expr a1, Expr a2, Expr a3);

	/** @deprecated */
	Call Call(Position pos, String name, Expr a1, Expr a2, Expr a3, Expr a4);

	/** @deprecated */
	Call Call(Position pos, String name, List args);

	/** @deprecated */
	Call Call(Position pos, Receiver target, String name);

	/** @deprecated */
	Call Call(Position pos, Receiver target, String name, Expr a1);

	/** @deprecated */
	Call Call(Position pos, Receiver target, String name, Expr a1, Expr a2);

	/** @deprecated */
	Call Call(Position pos, Receiver target, String name, Expr a1, Expr a2,
			Expr a3);

	/** @deprecated */
	Call Call(Position pos, Receiver target, String name, Expr a1, Expr a2,
			Expr a3, Expr a4);

	/** @deprecated */
	Call Call(Position pos, Receiver target, String name, List args);

	Case Default(Position pos);

	Case Case(Position pos, Expr expr);

	Cast Cast(Position pos, TypeNode type, Expr expr);

	Catch Catch(Position pos, Formal formal, Block body);

	CharLit CharLit(Position pos, char value);

	ClassBody ClassBody(Position pos, List members);

	ClassDecl ClassDecl(Position pos, Flags flags, Id name,
			TypeNode superClass, List interfaces, ClassBody body);

	/** @deprecated */
	ClassDecl ClassDecl(Position pos, Flags flags, String name,
			TypeNode superClass, List interfaces, ClassBody body);

	ClassLit ClassLit(Position pos, TypeNode typeNode);

	Conditional Conditional(Position pos, Expr cond, Expr consequent,
			Expr alternative);

	ConstructorCall ThisCall(Position pos, List args);

	ConstructorCall ThisCall(Position pos, Expr outer, List args);

	ConstructorCall SuperCall(Position pos, List args);

	ConstructorCall SuperCall(Position pos, Expr outer, List args);

	ConstructorCall ConstructorCall(Position pos, ConstructorCall.Kind kind,
			List args);

	ConstructorCall ConstructorCall(Position pos, ConstructorCall.Kind kind,
			Expr outer, List args);

	ConstructorDecl ConstructorDecl(Position pos, Flags flags, Id name,
			List formals, List throwTypes, Block body);

	/** @deprecated */
	ConstructorDecl ConstructorDecl(Position pos, Flags flags, String name,
			List formals, List throwTypes, Block body);

	FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, Id name);

	FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, Id name,
			Expr init);

	/** @deprecated */
	FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, String name);

	/** @deprecated */
	FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, String name,
			Expr init);

	Do Do(Position pos, Stmt body, Expr cond);

	Empty Empty(Position pos);

	Eval Eval(Position pos, Expr expr);

	Field Field(Position pos, Id name);

	Field Field(Position pos, Receiver target, Id name);

	/** @deprecated */
	Field Field(Position pos, String name);

	/** @deprecated */
	Field Field(Position pos, Receiver target, String name);

	FloatLit FloatLit(Position pos, FloatLit.Kind kind, double value);

	For For(Position pos, List inits, Expr cond, List iters, Stmt body);

	Formal Formal(Position pos, Flags flags, TypeNode type, Id name);

	/** @deprecated */
	Formal Formal(Position pos, Flags flags, TypeNode type, String name);

	If If(Position pos, Expr cond, Stmt consequent);

	If If(Position pos, Expr cond, Stmt consequent, Stmt alternative);

	Import Import(Position pos, Import.Kind kind, String name);

	Initializer Initializer(Position pos, Flags flags, Block body);

	Instanceof Instanceof(Position pos, Expr expr, TypeNode type);

	IntLit IntLit(Position pos, IntLit.Kind kind, long value);

	Labeled Labeled(Position pos, Id label, Stmt body);

	/** @deprecated */
	Labeled Labeled(Position pos, String label, Stmt body);

	Local Local(Position pos, Id name);

	/** @deprecated */
	Local Local(Position pos, String name);

	LocalClassDecl LocalClassDecl(Position pos, ClassDecl decl);

	LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, Id name);

	LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, Id name,
			Expr init);

	/** @deprecated */
	LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, String name);

	/** @deprecated */
	LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, String name,
			Expr init);

	MethodDecl MethodDecl(Position pos, Flags flags, TypeNode returnType,
			Id name, List formals, List throwTypes, Block body);

	/** @deprecated */
	MethodDecl MethodDecl(Position pos, Flags flags, TypeNode returnType,
			String name, List formals, List throwTypes, Block body);

	New New(Position pos, TypeNode type, List args);

	New New(Position pos, TypeNode type, List args, ClassBody body);

	New New(Position pos, Expr outer, TypeNode objectType, List args);

	New New(Position pos, Expr outer, TypeNode objectType, List args,
			ClassBody body);

	NewArray NewArray(Position pos, TypeNode base, List dims);

	NewArray NewArray(Position pos, TypeNode base, List dims, int addDims);

	NewArray NewArray(Position pos, TypeNode base, int addDims, ArrayInit init);

	NewArray NewArray(Position pos, TypeNode base, List dims, int addDims,
			ArrayInit init);

	NodeList NodeList(Position pos, List nodes);

	NodeList NodeList(Position pos, NodeFactory nf, List nodes);

	NullLit NullLit(Position pos);

	Return Return(Position pos);

	Return Return(Position pos, Expr expr);

	SourceCollection SourceCollection(Position pos, List sources);

	SourceFile SourceFile(Position pos, List decls);

	SourceFile SourceFile(Position pos, List imports, List decls);

	SourceFile SourceFile(Position pos, PackageNode packageName, List imports,
			List decls);

	Special This(Position pos);

	Special This(Position pos, TypeNode outer);

	Special Super(Position pos);

	Special Super(Position pos, TypeNode outer);

	Special Special(Position pos, Special.Kind kind);

	Special Special(Position pos, Special.Kind kind, TypeNode outer);

	StringLit StringLit(Position pos, String value);

	Switch Switch(Position pos, Expr expr, List elements);

	Synchronized Synchronized(Position pos, Expr expr, Block body);

	Throw Throw(Position pos, Expr expr);

	Try Try(Position pos, Block tryBlock, List catchBlocks);

	Try Try(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock);

	PackageNode PackageNode(Position pos, Package p);

	Unary Unary(Position pos, Unary.Operator op, Expr expr);

	Unary Unary(Position pos, Expr expr, Unary.Operator op);

	While While(Position pos, Expr cond, Stmt body);
}
