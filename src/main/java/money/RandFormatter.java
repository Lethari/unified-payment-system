package money;

import java.math.BigDecimal;
import java.util.Locale;

public final class RandFormatter {
    private RandFormatter() {
    }

    public static String format(BigDecimal amount) {
        return "R" + String.format(Locale.US, "%,.2f", amount);
    }
}
