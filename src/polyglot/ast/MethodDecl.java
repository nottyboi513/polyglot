package polyglot.ast;

import polyglot.types.MethodInstance;
import polyglot.types.Flags;
import java.util.List;

/**
 * A method declaration.
 */
public interface MethodDecl extends ProcedureDecl 
{
    /** The method's flags. */
    Flags flags();

    /** Set the method's flags. */
    MethodDecl flags(Flags flags);

    /** The method's return type.  */
    TypeNode returnType();

    /** Set the method's return type.  */
    MethodDecl returnType(TypeNode returnType);

    /** The method's name. */
    String name();

    /** Set the method's name. */
    MethodDecl name(String name);

    /** The method's formal parameters. */
    List formals();

    /** Set the method's formal parameters. */
    MethodDecl formals(List formals);

    /** The method's exception throw types. */
    List exceptionTypes();

    /** Set the method's exception throw types. */
    MethodDecl exceptionTypes(List exceptionTypes);


    /** The method's body. */
    Block body();

    /** Set the method's body. */
    MethodDecl body(Block body);

    /**
     * The method type object.  This field may not be valid until
     * after signature disambiguation.
     */
    MethodInstance methodInstance();

    /** Set the method's type object. */
    MethodDecl methodInstance(MethodInstance mi);
}
