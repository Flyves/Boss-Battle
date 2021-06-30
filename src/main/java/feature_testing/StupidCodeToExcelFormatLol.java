package feature_testing;

import java.util.Arrays;

public class StupidCodeToExcelFormatLol {
    public static void main(String[] args) {
        
        String thing = "aInverse.sample(new Vector2(-432.13079035626834, 2236.363424272832));\n" +
                "        aInverse.sample(new Vector2(-425.7666070079995, 2156.846194483909));\n" +
                "        aInverse.sample(new Vector2(-415.05901759516746, 2078.1071902603617));\n" +
                "        aInverse.sample(new Vector2(-402.63488015846633, 2000.3675301964288));\n" +
                "        aInverse.sample(new Vector2(-387.73142156625, 1923.8111990070909));\n" +
                "        aInverse.sample(new Vector2(-370.41075868423377, 1848.6095296645972));\n" +
                "        aInverse.sample(new Vector2(-351.71480011606036, 1774.9072317086868));\n" +
                "        aInverse.sample(new Vector2(-335.5965010621276, 1702.7366823275077));\n" +
                "        aInverse.sample(new Vector2(-326.0611240016351, 1631.5361584363447));\n" +
                "        aInverse.sample(new Vector2(-318.8772831933966, 1560.9836283014663));\n" +
                "        aInverse.sample(new Vector2(-311.59826106780656, 1491.020029592717));\n" +
                "        aInverse.sample(new Vector2(-313.0741290145352, 1421.2072761837462));\n" +
                "        aInverse.sample(new Vector2(-317.53960671039295, 1351.1535376093764));\n" +
                "        aInverse.sample(new Vector2(-320.6680836925534, 1280.7849394635275));\n" +
                "        aInverse.sample(new Vector2(-318.71093425528215, 1210.3740531590315));\n" +
                "        aInverse.sample(new Vector2(-310.6381826991219, 1140.4063242377147));\n" +
                "        aInverse.sample(new Vector2(-297.3450692060942, 1071.3663471174318));\n" +
                "        aInverse.sample(new Vector2(-278.8767011960135, 1003.7377333815409));\n" +
                "        aInverse.sample(new Vector2(-268.4028099535726, 937.3123349091497));\n" +
                "        aInverse.sample(new Vector2(-256.9420060127618, 871.7609008635313));\n" +
                "        aInverse.sample(new Vector2(-242.18842537997034, 807.3108041174687));\n" +
                "        aInverse.sample(new Vector2(-223.8350986221576, 744.2600883135395));\n" +
                "        aInverse.sample(new Vector2(-202.46955021775193, 682.9022446388982));\n" +
                "        aInverse.sample(new Vector2(-177.3264159835253, 623.5270575183818));\n" +
                "        aInverse.sample(new Vector2(-151.66257195785875, 566.2786071289737));\n" +
                "        aInverse.sample(new Vector2(-126.18547735339803, 511.168361620245));\n" +
                "        aInverse.sample(new Vector2(-108.26970622123895, 457.883213541434));\n" +
                "        aInverse.sample(new Vector2(-89.59716002672621, 406.0298175249391));\n" +
                "        aInverse.sample(new Vector2(-71.78573039706407, 355.69787105477303));\n" +
                "        aInverse.sample(new Vector2(-54.780846642861434, 306.81445791517484));\n" +
                "        aInverse.sample(new Vector2(-39.77314374300704, 259.2733669295878));\n" +
                "        aInverse.sample(new Vector2(-26.729311135957232, 212.8860533666768));\n" +
                "        aInverse.sample(new Vector2(-15.498809336994555, 167.46453583095078));\n" +
                "        aInverse.sample(new Vector2(-6.615763146162408, 122.89692562844226));\n" +
                "        aInverse.sample(new Vector2(0, 0));";
        Arrays.asList(thing.split(";"))
                .forEach(s -> {
                    int i1 = s.indexOf("new Vector2(")+12;
                    int i2 = s.lastIndexOf("))");
                    String jointNumber = s.substring(i1, i2);
                    String[] numbers = jointNumber.split(", ");

                    System.out.println(numbers[1] + " " + numbers[0]);
                });

    }
}
