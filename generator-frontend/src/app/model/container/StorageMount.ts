import { BaseContainer } from "../BaseContainer";
import { RandomWord } from "../../services/randomWord";

/**
 * Single Mount Point
 */
export class StorageMount extends BaseContainer {
    /** Mount Path of the storage */
    mountPath: string;

    /** Subfolder in the storage */
    subPath: string;

    /** Initializes the Storage Mount with an unique identifier */
    constructor() {
        super();
        this.uniqueName = RandomWord.getRandomWordCombination();
    }
}