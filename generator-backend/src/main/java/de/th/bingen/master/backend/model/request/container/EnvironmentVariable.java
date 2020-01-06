package de.th.bingen.master.backend.model.request.container;

import de.th.bingen.master.backend.helper.RandomWord;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Random;

public class EnvironmentVariable {
    @NotEmpty
    private String uniqueName;
    @NotEmpty
    @Pattern(regexp = "[-._a-zA-Z][-._a-zA-Z0-9]*")
    private String name;
    private String value;
    private boolean secret;

    public EnvironmentVariable() {
        this.uniqueName = "v-" + RandomWord.getRandomWordCombination();
    }

    public EnvironmentVariable(@NotEmpty String uniqueName, @NotEmpty @Pattern(regexp = "[-._a-zA-Z][-._a-zA-Z0-9]*") String name, @NotEmpty String value, boolean secret) {
        this.uniqueName = uniqueName;
        this.name = name;
        this.value = value;
        this.secret = secret;
    }

    public boolean isSecret() {
        return secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
}
