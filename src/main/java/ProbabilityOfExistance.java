public class ProbabilityOfExistance {
    private Double sum;
    private Double number;

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Double calculateProbability() {
        return sum / number;
    }

}
