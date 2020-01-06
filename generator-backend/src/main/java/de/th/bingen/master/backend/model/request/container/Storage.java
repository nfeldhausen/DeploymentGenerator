package de.th.bingen.master.backend.model.request.container;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?")
    private String name;
    @Positive
    private int size;
    @Valid
    @NotEmpty
    private List<StorageMount> storageMounts = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<StorageMount> getStorageMounts() {
        return storageMounts;
    }

    public void setStorageMounts(List<StorageMount> storageMounts) {
        this.storageMounts = storageMounts;
    }

    public void addStorageMount(String mountPath, String subPath) {
        storageMounts.add(new StorageMount(mountPath,subPath));
    }
}
