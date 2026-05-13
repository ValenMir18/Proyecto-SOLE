package sole.util;

public class Styles {

    public static final String BASE_CSS =
        "* { -fx-font-family: 'Arial Black', Arial, sans-serif; }" +

        ".root { -fx-background-color: #0d0d0d; }" +

        /* ── BRAND COLORS ── */
        ".accent  { -fx-text-fill: #c8ff00; }" +
        ".muted   { -fx-text-fill: #888888; }" +
        ".white   { -fx-text-fill: #ffffff; }" +

        /* ── BUTTONS ── */
        ".btn-primary {" +
        "  -fx-background-color: #c8ff00;" +
        "  -fx-text-fill: #000000;" +
        "  -fx-font-size: 13px;" +
        "  -fx-font-weight: bold;" +
        "  -fx-cursor: hand;" +
        "  -fx-padding: 12 24;" +
        "  -fx-background-radius: 4;" +
        "}" +
        ".btn-primary:hover { -fx-background-color: #b0e000; }" +

        ".btn-ghost {" +
        "  -fx-background-color: transparent;" +
        "  -fx-text-fill: #888888;" +
        "  -fx-border-color: #333333;" +
        "  -fx-border-radius: 4;" +
        "  -fx-background-radius: 4;" +
        "  -fx-font-size: 12px;" +
        "  -fx-cursor: hand;" +
        "  -fx-padding: 8 18;" +
        "}" +
        ".btn-ghost:hover { -fx-border-color: #c8ff00; -fx-text-fill: #c8ff00; }" +

        /* ── TEXT FIELD ── */
        ".field {" +
        "  -fx-background-color: #1a1a1a;" +
        "  -fx-text-fill: #ffffff;" +
        "  -fx-border-color: #333333;" +
        "  -fx-border-radius: 4;" +
        "  -fx-background-radius: 4;" +
        "  -fx-padding: 10 14;" +
        "  -fx-font-size: 13px;" +
        "  -fx-prompt-text-fill: #555555;" +
        "}" +
        ".field:focused { -fx-border-color: #c8ff00; }" +

        /* ── CARD ── */
        ".card {" +
        "  -fx-background-color: #151515;" +
        "  -fx-border-color: #222222;" +
        "  -fx-border-radius: 6;" +
        "  -fx-background-radius: 6;" +
        "  -fx-padding: 18;" +
        "}" +

        /* ── SIDEBAR ── */
        ".sidebar { -fx-background-color: #111111; -fx-border-color: #222222; -fx-border-width: 0 1 0 0; }" +

        ".nav-item {" +
        "  -fx-background-color: transparent;" +
        "  -fx-text-fill: #888888;" +
        "  -fx-font-size: 13px;" +
        "  -fx-cursor: hand;" +
        "  -fx-padding: 12 20;" +
        "  -fx-alignment: center-left;" +
        "  -fx-max-width: infinity;" +
        "}" +
        ".nav-item:hover { -fx-text-fill: #ffffff; -fx-background-color: #1a1a1a; }" +
        ".nav-item-active { -fx-background-color: #1e1e1e; -fx-text-fill: #c8ff00; -fx-border-color: #c8ff00; -fx-border-width: 0 0 0 3; }" +

        /* ── TOP BAR ── */
        ".topbar { -fx-background-color: #111111; -fx-border-color: #222222; -fx-border-width: 0 0 1 0; -fx-padding: 0 20; }" +

        /* ── STAT BOX ── */
        ".stat-box { -fx-background-color: #151515; -fx-border-color: #222222; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 16; }" +
        ".stat-num  { -fx-text-fill: #ffffff; -fx-font-size: 28px; -fx-font-weight: bold; }" +
        ".stat-label { -fx-text-fill: #666666; -fx-font-size: 11px; }" +

        /* ── TABLE ── */
        ".table-view { -fx-background-color: #151515; -fx-border-color: #222222; -fx-border-radius: 6; }" +
        ".table-view .column-header-background { -fx-background-color: #111111; }" +
        ".table-view .column-header, .table-view .filler { -fx-background-color: transparent; -fx-border-color: transparent; }" +
        ".table-view .column-header .label { -fx-text-fill: #666666; -fx-font-size: 10px; }" +
        ".table-view .table-row-cell { -fx-background-color: #151515; -fx-border-color: #1e1e1e; -fx-border-width: 0 0 1 0; }" +
        ".table-view .table-row-cell:selected { -fx-background-color: #1a1a1a; }" +
        ".table-view .table-cell { -fx-text-fill: #cccccc; -fx-font-size: 12px; -fx-alignment: center-left; }" +

        /* ── STATUS BADGES ── */
        ".badge-sent    { -fx-background-color: #1a3a5c; -fx-text-fill: #4dabf7; -fx-background-radius: 4; -fx-padding: 3 8; -fx-font-size: 10px; }" +
        ".badge-delivered { -fx-background-color: #1a3a1a; -fx-text-fill: #51cf66; -fx-background-radius: 4; -fx-padding: 3 8; -fx-font-size: 10px; }" +
        ".badge-pending { -fx-background-color: #3a2a00; -fx-text-fill: #fcc419; -fx-background-radius: 4; -fx-padding: 3 8; -fx-font-size: 10px; }" +
        ".badge-cancelled { -fx-background-color: #3a1a1a; -fx-text-fill: #ff6b6b; -fx-background-radius: 4; -fx-padding: 3 8; -fx-font-size: 10px; }" +

        /* ── PRODUCT CARD ── */
        ".product-card { -fx-background-color: #151515; -fx-border-color: #222222; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 16; -fx-cursor: hand; }" +
        ".product-card:hover { -fx-border-color: #c8ff00; }" +
        ".product-name { -fx-text-fill: #ffffff; -fx-font-size: 13px; -fx-font-weight: bold; }" +
        ".product-brand { -fx-text-fill: #666666; -fx-font-size: 10px; }" +
        ".product-price { -fx-text-fill: #c8ff00; -fx-font-size: 16px; -fx-font-weight: bold; }" +

        /* ── SCROLL ── */
        ".scroll-pane { -fx-background-color: transparent; -fx-background: transparent; }" +
        ".scroll-pane .viewport { -fx-background-color: transparent; }" +
        ".scroll-bar:vertical .thumb { -fx-background-color: #333333; -fx-background-radius: 4; }" +
        ".scroll-bar { -fx-background-color: #111111; }";

    private Styles() {}
}
