import { Board } from '../board';
import { Reader } from '../reader';
export class Writer {
    constructor(
        public id?: number,
        public board?: Board,
        public reader?: Reader,
    ) {
    }
}
