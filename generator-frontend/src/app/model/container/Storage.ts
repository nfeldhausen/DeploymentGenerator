import { BaseContainer } from "../BaseContainer";
import { RandomWord } from "../../services/randomWord";
import { StorageMount } from "./StorageMount";
import { dataType } from "angular-http-deserializer/decorators";

/**
 * Storage for the container
 */
export class Storage extends BaseContainer {
    /* Name of the PVC */
    name: string;

    /** Size of the PVC */
    size: number;

    /** Mount points of the storage */
    @dataType(StorageMount, true)
    storageMounts: StorageMount[] = [];

    /**
     * Initializes the storage with an unique identifier and adds an empty mount point
     */
    constructor() {
        super();
        this.uniqueName = RandomWord.getRandomWordCombination();
        this.storageMounts.push(new StorageMount());
    }

    /**
     * Adds an empty mount point
     */
    addMount(): void {
        this.storageMounts.push(new StorageMount());
    }

    /**
     * Deletes a mount point
     * 
     * @param mount 
     */
    deleteMount(mount: StorageMount): void {
        this.storageMounts = this.storageMounts.filter(m => m !== mount);
    }

    /**
     * Changes the button icon and colour depending on if it's the last element in the storage mount list
     * 
     * @param event The HTML Button event
     * @param mount The storage mount, if it needs to be deleted (depending on button class)
     */
    mountManipulation(event: Event, mount: StorageMount): void {
        let element = <HTMLButtonElement>event.currentTarget;

        if (element.classList.contains('btn-primary')) {
            element.classList.remove('btn-primary');
            element.classList.add('btn-danger');
            element.innerHTML = '<i class="fas fa-minus" title="Add Volume Mount">';

            this.addMount();
        } else {
            element.classList.add('btn-primary');
            element.classList.remove('btn-danger');
            element.innerHTML = '<i class="fas fa-plus" title="Delete Volume Mount">';

            this.deleteMount(mount);
        }
    }
}