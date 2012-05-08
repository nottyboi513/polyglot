package polyglot.ext.jl5.ast;

import java.util.*;

import polyglot.ast.*;
import polyglot.ext.jl5.types.EnumInstance;
import polyglot.ext.jl5.types.JL5ParsedClassType;
import polyglot.ext.jl5.types.JL5TypeSystem;
import polyglot.types.*;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.*;

public class EnumConstantDecl_c extends Term_c implements EnumConstantDecl {
	protected List args;
	protected Id name;
	protected Flags flags;
	protected ClassBody body;
	protected EnumInstance enumInstance;
	protected ConstructorInstance constructorInstance;
	protected ParsedClassType type;
	protected long ordinal;

	public EnumConstantDecl_c(Position pos, Flags flags, Id name, List args,
			ClassBody body) {
		super(pos);
		this.name = name;
		this.args = args;
		this.body = body;
		this.flags = flags;
	}

	@Override
	public long ordinal() {
		return ordinal;
	}

	public EnumConstantDecl ordinal(long ordinal) {
		EnumConstantDecl_c n = (EnumConstantDecl_c) copy();
		n.ordinal = ordinal;
		return n;
	}

	public MemberInstance memberInstance() {
		return enumInstance;
	}

	/** get args */
	@Override
	public List args() {
		return args;
	}

	/** set args */
	@Override
	public EnumConstantDecl args(List args) {
		EnumConstantDecl_c n = (EnumConstantDecl_c) copy();
		n.args = args;
		return n;
	}

	/** set name */
	public EnumConstantDecl name(Id name) {
		EnumConstantDecl_c n = (EnumConstantDecl_c) copy();
		n.name = name;
		return n;
	}

	/** get name */
	@Override
	public Id name() {
		return name;
	}

	/** set body */
	@Override
	public EnumConstantDecl body(ClassBody body) {
		EnumConstantDecl_c n = (EnumConstantDecl_c) copy();
		n.body = body;
		return n;
	}

	/** get body */
	@Override
	public ClassBody body() {
		return body;
	}

	@Override
	public ParsedClassType type() {
		return type;
	}

	@Override
	public Flags flags() {
		return flags;
	}

	@Override
	public EnumConstantDecl type(ParsedClassType pct) {
		EnumConstantDecl_c n = (EnumConstantDecl_c) copy();
		n.type = pct;
		return n;
	}

	@Override
	public EnumConstantDecl enumInstance(EnumInstance ei) {
		EnumConstantDecl_c n = (EnumConstantDecl_c) copy();
		n.enumInstance = ei;
		return n;
	}

	@Override
	public EnumInstance enumInstance() {
		return enumInstance;
	}

	@Override
	public EnumConstantDecl constructorInstance(ConstructorInstance ci) {
		EnumConstantDecl_c n = (EnumConstantDecl_c) copy();
		n.constructorInstance = ci;
		return n;
	}

	@Override
	public ConstructorInstance constructorInstance() {
		return constructorInstance;
	}

	protected EnumConstantDecl_c reconstruct(List args, ClassBody body) {
		if (!CollectionUtil.equals(args, this.args) || body != this.body) {
			EnumConstantDecl_c n = (EnumConstantDecl_c) copy();
			n.args = TypedList.copyAndCheck(args, Expr.class, true);
			n.body = body;
			return n;
		}
		return this;
	}

	@Override
	public Node visitChildren(NodeVisitor v) {
		List args = visitList(this.args, v);
		ClassBody body = (ClassBody) visitChild(this.body, v);
		return reconstruct(args, body);
	}

	public Context enterChildScope(Node child, Context c) {
		if (child == body && type != null && body != null) {
			c = c.pushClass(type, type);
		}
		return super.enterChildScope(child, c);
	}

	@Override
	public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
		if (body() != null)
			return tb.pushCode().pushAnonClass(position());
		else
			return tb.pushCode();
	}

	@Override
	public Node buildTypes(TypeBuilder tb) throws SemanticException {
		JL5TypeSystem ts = (JL5TypeSystem) tb.typeSystem();

		List l = new ArrayList(args().size());
		for (int i = 0; i < args().size(); i++) {
			l.add(ts.unknownType(position()));
		}

		ConstructorInstance ci = ts.constructorInstance(position(),
				ts.Object(), Flags.NONE, l, Collections.EMPTY_LIST);

		EnumConstantDecl n = constructorInstance(ci);
		JL5ParsedClassType enumType = null;
		if (n.body() != null) {
			ParsedClassType type = tb.currentClass();
			n = n.type(type);
			type.setMembersAdded(true);
			enumType = (JL5ParsedClassType) tb.pop().currentClass();

			if (!type.supertypesResolved()) {
				type.superType(enumType);
				type.setSupertypesResolved(true);
			}

		} else {
			// this is not an anonymous class extending the enum
			enumType = (JL5ParsedClassType) tb.currentClass();
			n = n.type(enumType);
		}

		// now add the appropriate enum declaration to the containing class
		if (enumType == null) {
			return n;
		}
		EnumInstance ei = ts.enumInstance(position(), enumType, Flags.NONE,
				name.id(), n.type(), ordinal);
		enumType.addEnumConstant(ei);
		n = n.enumInstance(ei);

		return n;
	}

	@Override
	public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
		// Everything is done in disambiguateOverride.
		return this;
	}

	@Override
	public Node disambiguateOverride(Node parent, AmbiguityRemover ar)
			throws SemanticException {
		EnumConstantDecl nn = this;
		EnumConstantDecl old = nn;

		BodyDisambiguator bd = new BodyDisambiguator(ar);
		NodeVisitor childv = bd.enter(parent, this);

		if (childv instanceof PruningVisitor) {
			return nn;
		}

		BodyDisambiguator childbd = (BodyDisambiguator) childv;

		// Now disambiguate the actuals.
		nn = (EnumConstantDecl) nn.args(nn.visitList(nn.args(), childbd));
		if (childbd.hasErrors())
			throw new SemanticException();

		if (nn.body() != null) {
			ParsedClassType anonType = nn.type();

			SupertypeDisambiguator supDisamb = new SupertypeDisambiguator(
					childbd);
			nn = nn.body((ClassBody) nn.visitChild(nn.body(), supDisamb));
			if (supDisamb.hasErrors())
				throw new SemanticException();

			SignatureDisambiguator sigDisamb = new SignatureDisambiguator(
					childbd);
			nn = nn.body((ClassBody) nn.visitChild(nn.body(), sigDisamb));
			if (sigDisamb.hasErrors())
				throw new SemanticException();

			// Now visit the body.
			nn = nn.body((ClassBody) nn.visitChild(nn.body(), childbd));
			if (childbd.hasErrors())
				throw new SemanticException();
		}

		nn = (EnumConstantDecl) bd.leave(parent, old, nn, childbd);

		return nn;
	}

	@Override
	public Node typeCheck(TypeChecker tc) throws SemanticException {
		JL5TypeSystem ts = (JL5TypeSystem) tc.typeSystem();
		Context c = tc.context();
		JL5ParsedClassType ct = (JL5ParsedClassType) c.currentClass();

		List argTypes = new LinkedList();
		for (Iterator it = this.args.iterator(); it.hasNext();) {
			Expr e = (Expr) it.next();
			argTypes.add(e.type());
		}

		ConstructorInstance ci = ts.findConstructor(ct, argTypes,
				c.currentClass());
		EnumConstantDecl_c n = (EnumConstantDecl_c) this
				.constructorInstance(ci);

		if (n.flags() != Flags.NONE) {
			throw new SemanticException("Cannot have modifier(s): " + flags
					+ " on enum constant declaration", this.position());
		}

		if (this.body != null) {
			ts.checkClassConformance(type);
		}

		return n;
	}

	@Override
	public String toString() {
		return name + "(" + args + ")" + body != null ? "..." : "";
	}

	@Override
	public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
		w.write(name.id());
		if (args != null && !args.isEmpty()) {
			w.write(" ( ");
			Iterator it = args.iterator();
			while (it.hasNext()) {
				Expr e = (Expr) it.next();
				print(e, w, tr);
				if (it.hasNext()) {
					w.write(", ");
					w.allowBreak(0);
				}
			}
			w.write(" )");
		}
		if (body != null) {
			w.write(" {");
			print(body, w, tr);
			w.write("}");
		}
	}

	@Override
	public List acceptCFG(CFGBuilder v, List succs) {
		return succs;
	}

	@Override
	public Term firstChild() {
		return this;
	}
}
