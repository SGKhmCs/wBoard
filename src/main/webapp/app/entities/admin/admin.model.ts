import { Board } from '../board';
import { Writer } from '../writer';
export class Admin {
    constructor(
        public id?: number,
        public board?: Board,
        public writer?: Writer,
    ) {
    }
}
