import { Board } from '../board';
import { User } from '../../shared';
export class Reader {
    constructor(
        public id?: number,
        public board?: Board,
        public user?: User,
    ) {
    }
}
