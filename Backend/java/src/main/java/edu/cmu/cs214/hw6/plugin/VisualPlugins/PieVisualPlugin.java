package edu.cmu.cs214.hw6.plugin.VisualPlugins;

import edu.cmu.cs214.hw6.framework.core.ContentClassifyFramework;
import edu.cmu.cs214.hw6.framework.core.VisualPlugin;
import org.icepear.echarts.Bar;
import org.icepear.echarts.Pie;
import org.icepear.echarts.charts.bar.BarSeries;
import org.icepear.echarts.charts.pie.PieDataItem;
import org.icepear.echarts.render.Engine;

import java.util.ArrayList;

public class PieVisualPlugin implements VisualPlugin {
    private static final String VISUAL_NAME = "| Pie chart |";
    private ContentClassifyFramework framework;

    @Override
    public String getVisualName() {
        return VISUAL_NAME;
    }

    @Override
    public void onRegister(ContentClassifyFramework framework) {
        this.framework = framework;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onClosed() {

    }

//    public void setXLabelNames() {
//        this.xLabelNames = (String[]) framework.getTopicFreqMap().keySet().toArray();
//    }
//
//    public void setYValues() {
//
//    }

    @Override
    public void visualizeData() {

        int size = framework.getTopicFreqMap().keySet().size();
        ArrayList<PieDataItem> pieDataItemList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Number curValue = (Number) framework.getTopicFreqMap().values().toArray()[i];
            String curKey = framework.getTopicFreqMap().keySet().toArray(new String[0])[i];
            pieDataItemList.add(
                    new PieDataItem().setValue(curValue).setName(curKey)
            );
        }


        String[] arr = framework.getTopicFreqMap().keySet().toArray(new String[0]);
        Pie pie = new Pie()
                .setTitle("Basic Pie")
                .setTooltip("item")
                .setLegend()
                .addSeries(pieDataItemList.toArray());
        Engine engine = new Engine();
        // The render method will generate our EChart into a HTML file saved locally in the current directory.
        // The name of the HTML can also be set by the first parameter of the function.
        engine.render(SAVE_PATH, pie);

    }
}
