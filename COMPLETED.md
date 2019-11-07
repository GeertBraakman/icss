# Assesment

In dit document laat ik alles zien wat ik heb gedaan voor het assesment. 

## Door School aangeleverde eisen.

Voldoet | ID | Omschrijving | Prio | Punten | 
--- | --- | --- | --- | ---
**Algemeen** | |
 Ja | AL01 | De code behoudt de packagestructuur van de aangeleverde startcode. | Must | 0
 Ja | AL02 | Alle code compileert en is te bouwen met Maven | Must | 0
 Ja | AL03 | De code is goed geformatteerd, zo nodig voorzien van commentaar, correcte variabelenamen gebruikt, bevat geen onnodig ingewikkelde constructies en is zo onderhoudbaar mogelijk opgested. (naar oordeel van docent) | Must | 0
 **Parsen** | |
 Ja | PA01 | Implementeer een parser plus listener die AST’s kan maken voor ICSS documenten die “eenvoudige opmaak” kan parseren, zoals beschreven in de taalbeschrijving. In `level0.icss` vind je een voorbeeld van ICSS code die je moet kunnen parseren. `testParseLevel0()` slaagt. | Must | 10
 Ja | PA02 | Breid je grammatica en listener uit zodat nu ook assignments van variabelen en het gebruik ervan geparseerd kunnen worden. In `level1.icss` vind je voorbeeldcode die je nu zou moeten kunnen parseren. `testParseLevel1()` slaagt. | Must | 10
 Ja | PA03 | Breid je grammatica en listener uit zodat je nu ook optellen en aftrekken en vermenigvuldigen kunt parseren. In `level2.icss` vind je voorbeeld- code die je nu ook zou moeten kunnen parseren. `testParseLevel2()` slaagt. | Should | 10
 Ja | PA04 | Breid je grammatica en listener uit zodat je if-statements aankunt. In `level3.icss` vind je voorbeeldcode die je nu ook zou moeten kunnen parseren. `testParseLevel3()` slaagt. | Should | 10
 **Checker** | | 
 Ja | CH00 | Minimaal twee van de onderstaande checks **moeten** zijn geïmplementeerd. | Must | 0
 Ja | CH01 | Controleer of er geen variabelen worden gebruikt die niet gedefinieerd zijn. | Should | 4
 Ja | CH02 | Controleer of de operanden van de operaties plus en min van gelijk type zijn en dat vermenigvuldigen enkel met scalaire waarden gebeurt. Je mag geen pixels bij percentages optellen bijvoorbeeld. | Should | 4
 Ja | CH03 | Controleer of er geen kleuren worden gebruikt in operaties (plus, min en keer). | Should | 4
 Ja | CH04 | Controleer of bij declaraties het type van de waarde klopt bij de stijleigenschap. Declaraties zoals width: #ff0000 of color: 12px zijn natuurlijk onzin. | Should | 4
 Ja | CH05 | Controleer of de conditie bij een if-statement van het type boolean is (zowel bij een variabele-referentie als een boolean literal) | Should | 4
 **Transformeren**| |
 Ja | TR01 | Implementeer de `EvalExpressions` transformatie. Deze transformatie vervangt alle `Expression` knopen in de AST door een `Literal` knoop met de berekende waarde. | Should | 10
 Ja TR02 | Implementeer de `RemoveIf` transformatie. Deze transformatie verwijdert alle `IfClauses` uit de AST. Wanneer de conditie van de `IfClause` `TRUE` is wordt deze vervangen door de body van het if-statement. Als de conditie `FALSE` is dan verwijder je de `IfClause` volledig uit de AST. | Should | 10
 **Genereren** | | 
 Ja | GE01 | Implementeer de generator in `Generator` die de AST naar een CSS2-compliant string omzet. | Must | 5
 Ja | GE02 | Voor zover nodig, verbeter je generator zo dat de stijlregels met nette indenting geprint worden. | Should | 5
 
 ## Eigen uitbreidingen

Voldoet | Omschrijving | Punten | 
--- | --- | --- 
Ja | De scopes zijn juist geimplementeerd in de checker en de transformer | 10
Ja | Een stylerule kan meerdere selectors hebben | 3
Ja | Een `ifClause` kan een `elseClause` bevatten die wordt toegevoegt aan de AST als de `ifClause` `FALSE` als expressie heeft. | 5
Ja | Het is niet mogelijk om te rekenen met Booleans | 0
Ja | Het gegenereede css bestand heeft een Watermerk boven aan het bestand. | 0
