package com.opengg.wars.components;

import com.opengg.core.GGInfo;
import com.opengg.core.math.Tuple;
import com.opengg.core.util.GGInputStream;
import com.opengg.core.util.GGOutputStream;
import com.opengg.wars.SimonWars;
import com.opengg.wars.game.Empire;
import com.opengg.wars.game.GameResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceProducer extends Building{
    public List<Tuple<List<Tuple<GameResource, Integer>>, Tuple<GameResource, Integer>>> products = new ArrayList<>();
    public int selected=0;

    float timer = 0;

    boolean outSet = false;
    boolean inSet = false;

    public ResourceProducer(){

    }

    public ResourceProducer(Empire.Side side) {
        super(side);
    }

    public void addOutput(GameResource out, int amount, Tuple<GameResource, Integer>... inputs){
        products.add(Tuple.of(List.of(inputs), Tuple.of(out, amount)));
    }


    @Override
    public void update(float delta){
        if(GGInfo.isServer() || SimonWars.offline){
            timer += delta;
            if(timer > 1){
                timer = 0;
                var product = products.get(selected);
                    var works = product.x.stream()
                            .allMatch(c -> Empire.get(side).getAvailable(c.x) >= c.y);
                    if(works){
                        product.x.forEach(c -> Empire.get(side).use(c.x,c.y));
                        Empire.get(side).add(product.y.x, product.y.y);
                    }
            }
        }
    }

    @Override
    public void serialize(GGOutputStream out) throws IOException {
        super.serialize(out);
        out.write(products.size());
        for(var product : products){
            out.write(product.y.x.ordinal());
            out.write(product.y.y);
            out.write(product.x.size());
            for(var input : product.x){
                out.write(input.x.ordinal());
                out.write(input.y);
            }
        }
    }

    @Override
    public void serializeUpdate(GGOutputStream out) throws IOException{
        super.serializeUpdate(out);
        out.write(selected);
    }

    @Override
    public void deserialize(GGInputStream in) throws IOException {
        super.deserialize(in);
        var prodSize = in.readInt();
        for(int i = 0; i < prodSize; i++){
            var prodType = GameResource.values()[in.readInt()];
            var prodAmount = in.readInt();
            var priceSize = in.readInt();
            var amount = new ArrayList<Tuple<GameResource, Integer>>();
            for(int j = 0; j < priceSize; j++) {
                var priceType = GameResource.values()[in.readInt()];
                var priceAmount = in.readInt();
                amount.add(Tuple.of(priceType, priceAmount));
            }

            products.add(Tuple.of(amount, Tuple.of(prodType,prodAmount)));
        }
    }

    @Override
    public void deserializeUpdate(GGInputStream in, float delta) throws IOException{
        super.deserializeUpdate(in, delta);
        selected = in.readInt();
    }
}
