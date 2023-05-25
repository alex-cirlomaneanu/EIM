package ro.pub.cs.systems.eim.pokemon;

import androidx.annotation.NonNull;

public class PokemonInfo {
    String abilities;
    String types;
    String image;

    public PokemonInfo(String abilities, String types, String image) {
        this.abilities = abilities;
        this.types = types;
        this.image = image;
    }

    public String getAbilities() {
        return abilities;
    }

    public String getTypes() {
        return types;
    }

    public String getImage() {
        return image;
    }

    @NonNull
    @Override
    public String toString() {
        return types + "\n" + abilities ;
    }
}
