package view;

import controller.ReservationDAO;
import controller.TrajetDAO;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import javax.swing.*;

public class HomePanel extends JPanel {

    public HomePanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Disposition verticale des graphiques

        // Graphique en aires pour les Bus
        XYChart areaChartBus = createAreaChart();
        this.add(new XChartPanel<>(areaChartBus));

        // Graphique en ligne pour les Trajets
        XYChart lineChartTrajets = createLineChart("Nombre de Trajets", TrajetDAO.getNombreDeTrajets());
        this.add(new XChartPanel<>(lineChartTrajets));

        // Graphique de dispersion pour les Réservations
        XYChart scatterChartReservations = createScatterChart("Nombre de Réservations", ReservationDAO.getNombreDeReservations());
        this.add(new XChartPanel<>(scatterChartReservations));
    }

    private XYChart createAreaChart() {
        double[] xData = new double[] {1, 2, 3, 4, 5}; // Par exemple, représente le temps
        double[] yData = new double[] {50, 100, 150, 200, 250}; // Mettez ici les valeurs correspondantes

        // Création du graphique
        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Évolution du Nombre de Bus")
                .xAxisTitle("Temps")
                .yAxisTitle("Nombre de Bus")
                .build();

        // Personnalisation du style du graphique
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

        // Ajout des données au graphique
        XYSeries series = chart.addSeries("Nombre de Bus", xData, yData);

        return chart;
    }

    private XYChart createLineChart(String title, int value) {
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title).xAxisTitle("Temps").yAxisTitle("Nombre").build();
        chart.addSeries("Trajets", new double[]{1, 2, 3, 4, 5}, new double[]{value, value, value, value, value});
        return chart;
    }

    private XYChart createScatterChart(String title, int value) {
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title).xAxisTitle("Catégorie").yAxisTitle("Nombre").build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.addSeries("Réservations", new double[]{1, 2, 3, 4, 5}, new double[]{value, value, value, value, value});
        return chart;
    }
}
