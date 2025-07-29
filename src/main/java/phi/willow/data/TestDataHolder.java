package phi.willow.data;

import com.mojang.serialization.Codec;

public class TestDataHolder {

    public static final Codec<TestDataHolder> CODEC = Codec.INT.xmap(
            TestDataHolder::new,
            holder -> holder.testNumber
    );
    public int testNumber = 0;
    public TestDataHolder(int number) {
        this.testNumber = number;
    }
}
