package benchmarking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LatencyData {
    private String sourceLan;
    private long latency;
    private int textLength;
}
