package com.nextbank.cli.helper;

import com.nextbank.cli.domain.AccountOperation;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class ConsoleHelper {

    private final static String ANSI_YELLOW = "\u001B[33m";
    private final static String ANSI_RESET = "\u001B[0m";
    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private final PrintStream out = System.out;

    public void write(List<AccountOperation> operations) {
        this.out.println("> ");
        this.out.print(ANSI_YELLOW);
        this.out.println(" ---------------- ---------------------- ------------ ------------");
        this.out.println("| Operation type |          Date        |    Amount  |   Balance  |");
        this.out.println("|----------------|----------------------|------------|------------|");
        if (operations == null || operations.isEmpty()) {
            this.out.println("|                            No operation                         |");
        } else {
            operations.forEach(operation -> this.out.println(this.print(operation)));
        }
        this.out.print(" ---------------- ---------------------- ------------ ------------");
        this.out.print(ANSI_RESET);
        this.out.println();
    }

    public void write(String msg, String... args) {
        this.out.print("> ");
        this.out.print(ANSI_YELLOW);
        this.out.printf(msg, (Object[]) args);
        this.out.print(ANSI_RESET);
        this.out.println();
    }

    private String print(AccountOperation operation) {
        final StringBuilder sb = new StringBuilder();
        sb.append("|").append(this.centralizeText(operation.getType().name(), 16));
        sb.append("|").append(this.centralizeText(dateFormat.format(operation.getDate()), 22));
        sb.append("|").append(this.centralizeText(operation.getAmount().toString(), 12));
        sb.append("|").append(this.centralizeText(operation.getBalance().toString(), 12));
        sb.append("|");
        return sb.toString();
    }

    private String centralizeText(String text, int columnLength) {
        int nbSpace;
        if (text.length() % 2 == 0) {
            nbSpace = (columnLength - text.length()) / 2;
            String space = makeSpace(nbSpace);
            return space + text + space;
        } else {
            nbSpace = (columnLength - text.length() - 1) / 2;
            String leftSpace = makeSpace(nbSpace);
            String rightSpace = makeSpace(nbSpace + 1);
            return leftSpace + text + rightSpace;
        }
    }

    private String makeSpace(int nbSpace) {
        final StringBuilder sb = new StringBuilder();
        IntStream.range(1, nbSpace + 1).forEach(num -> sb.append(" "));
        return sb.toString();
    }
}