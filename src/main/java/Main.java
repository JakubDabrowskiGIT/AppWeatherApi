public class Main {
    /*Zadanie po każdym zadaniu należy wykonać commit:
    1. Zaimplementuj interfejs Runnable, usuń metody statyczne oraz wydziel klase uruchomieniową
    2. Parametrami pobieranymi oraz wyświetlanymi z api mają być: temperatura, temperatura maksymalna, temperatura średnia,
    zachmurzenie, wiatr, ciśnienie, widoczność, opis pogody oraz ich odpowiednie jednostki.
    3. Należy dołożyć możliwość pobierania pogody dla miejsca poprzez parametry kodu pocztowego, koordynatów oraz po id miasta
    4. Aplikacja po pobraniu oraz wyświetleniu pogody ma się uruchamiać ponownie.
    5. Aplikacja ma mieć możliwość wyboru metody pobrania miejsca dla którego ma zostać wyświetlona pogoda
    6. Aplikacja ma mieć możliwość zamknięcia z poziomu konsoli
    7. Napisać testy jednostkowe
    8. Aplikacja ma mieć możliwość wyświetlenia pogody na od 1 do 16 dni
    9. Aplikacja ma mieć możliwość pobrania miejsca na ww. metody oraz
    możliwość wpisania liczby dni dla których ma zostać wyświetlona pogoda
    */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        mainApp.run();
    }
}
