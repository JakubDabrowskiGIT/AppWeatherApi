import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Weather {
    private String temp;
    private Double humidity;
    private Integer clouds;
    private Integer pressure;
    private Double wind;
    private String description;
    private String date;

    @Override
    public String toString() {
        return "Pogoda dla miasta to: " +
                "Temperatura: " + temp + " \u00b0C" +
                " Wilgotność: " + humidity + " %" +
                " Zachmurzenie: " + clouds + "%" +
                " Ciśnienie: " + pressure + " hPa";
    }
}
