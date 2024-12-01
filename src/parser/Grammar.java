package parser;

import java.util.HashMap;
import java.util.Map;

public class Grammar {
    public static final String EPSILON = "ε";
    public static final String ID = "id";
    public static final String NUM = "num";
    public static final String PRODUCTION = "->";
    public static final String START_SYMBOL = "MAIN";
    public static final String ATTRIBUTION_PRIME_SYMBOL = "ATRIBST'";
    public static final String FUNCTION_CALL_ATTRIBUTION = "idFCALL";
    public static final String EXPRESSION_ATTRIBUTION = "idEXPR";

    public Map<String, Map<String, String>> getTable() {
        Map<String, Map<String, String>> table = new HashMap<>();

        table.put("MAIN", Map.of(
                "$", "MAIN -> ε",
                "def", "MAIN -> FLIST",
                "id", "MAIN -> STMT",
                "{", "MAIN -> STMT",
                "int", "MAIN -> STMT",
                ";", "MAIN -> STMT",
                "print", "MAIN -> STMT",
                "return", "MAIN -> STMT",
                "if", "MAIN -> STMT"
        ));

        table.put("FLIST", Map.of(
                "def", "FLIST -> FDEF FLIST'"
        ));

        table.put("FLIST'", Map.of(
                "$", "FLIST' -> ε",
                "def", "FLIST' -> FLIST"
        ));

        table.put("FDEF", Map.of(
                "def", "FDEF -> def id ( PARLIST ) { STMTLIST }"
        ));

        table.put("PARLIST", Map.of(
                ")", "PARLIST -> ε",
                "int", "PARLIST -> int id PARLIST'"
        ));

        table.put("PARLIST'", Map.of(
                ")", "PARLIST' -> ε",
                ",", "PARLIST' -> , PARLIST"
        ));

        table.put("VARLIST", Map.of(
                "id", "VARLIST -> id VARLIST'"
        ));

        table.put("VARLIST'", Map.of(
                ",", "VARLIST' -> , VARLIST",
                ";", "VARLIST' -> ε"
        ));

        table.put("STMT", Map.of(
                "id", "STMT -> ATRIBST ;",
                "{", "STMT -> { STMTLIST }",
                "int", "STMT -> int VARLIST ;",
                ";", "STMT -> ;",
                "print", "STMT -> PRINTST ;",
                "return", "STMT -> RETURNST ;",
                "if", "STMT -> IFSTMT"
        ));

        table.put("ATRIBST", Map.of(
                "id", "ATRIBST -> id := ATRIBST'"
        ));

        table.put("ATRIBST'", Map.of(
                "idEXPR", "ATRIBST' -> EXPR",
                "idFCALL", "ATRIBST' -> FCALL",
                "(", "ATRIBST' -> EXPR",
                "num", "ATRIBST' -> EXPR"
        ));

        table.put("FCALL", Map.of(
                "id", "FCALL -> id ( PARLISTCALL )"
        ));

        table.put("PARLISTCALL", Map.of(
                "id", "PARLISTCALL -> id PARLISTCALL'",
                ")", "PARLISTCALL -> ε"
        ));

        table.put("PARLISTCALL'", Map.of(
                ")", "PARLISTCALL' -> ε",
                ",", "PARLISTCALL' -> , PARLISTCALL"
        ));

        table.put("PRINTST", Map.of(
                "print", "PRINTST -> print EXPR"
        ));

        table.put("RETURNST", Map.of(
                "return", "RETURNST -> return RETURNST'"
        ));

        table.put("RETURNST'", Map.of(
                "id", "RETURNST' -> id",
                ";", "RETURNST' -> ε"
        ));

        table.put("IFSTMT", Map.of(
                "if", "IFSTMT -> if ( EXPR ) STMT IFSTMT'"
        ));

        table.put("IFSTMT'", Map.of(
                "$", "IFSTMT' -> ε",
                "id", "IFSTMT' -> ε",
                "{", "IFSTMT' -> ε",
                "}", "IFSTMT' -> ε",
                "int", "IFSTMT' -> ε",
                ";", "IFSTMT' -> ε",
                "print", "IFSTMT' -> ε",
                "return", "IFSTMT' -> ε",
                "if", "IFSTMT' -> ε",
                "else", "IFSTMT' -> else STMT"
        ));

        table.put("STMTLIST", Map.of(
               "id", "STMTLIST -> STMT STMTLIST'",
               "{", "STMTLIST -> STMT STMTLIST'",
               "int", "STMTLIST -> STMT STMTLIST'",
               ";", "STMTLIST -> STMT STMTLIST'",
               "print", "STMTLIST -> STMT STMTLIST'",
               "return", "STMTLIST -> STMT STMTLIST'",
               "if", "STMTLIST -> STMT STMTLIST'"
        ));

        table.put("STMTLIST'", Map.of(
                "id", "STMTLIST' -> STMTLIST",
                "{", "STMTLIST' -> STMTLIST",
                "}", "STMTLIST' -> ε",
                "int", "STMTLIST' -> STMTLIST",
                ";", "STMTLIST' -> STMTLIST",
                "print", "STMTLIST' -> STMTLIST",
                "return", "STMTLIST' -> STMTLIST",
                "if", "STMTLIST' -> STMTLIST"
        ));

        table.put("EXPR", Map.of(
                "id", "EXPR -> NUMEXPR EXPR'",
                "(", "EXPR -> NUMEXPR EXPR'",
                "num", "EXPR -> NUMEXPR EXPR'"
        ));

        table.put("EXPR'", Map.of(
                ")", "EXPR' -> ε",
                ";", "EXPR' -> ε",
                "<", "EXPR' -> < LESSTHAN",
                ">", "EXPR' -> > GREATERTHAN",
                "==", "EXPR' -> == NUMEXPR"
        ));

        table.put("LESSTHAN", Map.of(
                "id", "LESSTHAN -> NUMEXPR",
                "(", "LESSTHAN -> NUMEXPR",
                ">", "LESSTHAN -> > NUMEXPR",
                "=", "LESSTHAN -> = NUMEXPR",
                "num", "LESSTHAN -> NUMEXPR"
        ));

        table.put("GREATERTHAN", Map.of(
                "id", "GREATERTHAN -> NUMEXPR",
                "(", "GREATERTHAN -> NUMEXPR",
                "=", "GREATERTHAN -> = NUMEXPR",
                "num", "GREATERTHAN -> NUMEXPR"
        ));

        table.put("NUMEXPR", Map.of(
                "id", "NUMEXPR -> TERM NUMEXPR'",
                "(", "NUMEXPR -> TERM NUMEXPR'",
                "num", "NUMEXPR -> TERM NUMEXPR'"
        ));

        table.put("NUMEXPR'", Map.of(
                ")", "NUMEXPR' -> ε",
                ";", "NUMEXPR' -> ε",
                "<", "NUMEXPR' -> ε",
                ">", "NUMEXPR' -> ε",
                "==", "NUMEXPR' -> ε",
                "+", "NUMEXPR' -> + TERM NUMEXPR'",
                "-", "NUMEXPR' -> - TERM NUMEXPR'"
        ));

        table.put("TERM", Map.of(
                "id", "TERM -> FACTOR TERM'",
                "(", "TERM -> FACTOR TERM'",
                "num", "TERM -> FACTOR TERM'"
        ));

        table.put("TERM'", Map.of(
                ")", "TERM' -> ε",
                ";", "TERM' -> ε",
                "<", "TERM' -> ε",
                ">", "TERM' -> ε",
                "==", "TERM' -> ε",
                "+", "TERM' -> ε",
                "-", "TERM' -> ε",
                "*", "TERM' -> * FACTOR TERM'",
                "/", "TERM' -> / FACTOR TERM'"
        ));

        table.put("FACTOR", Map.of(
                "id", "FACTOR -> id",
                "(", "FACTOR -> ( NUMEXPR )",
                "num", "FACTOR -> num"
        ));

        return table;
    }
}
