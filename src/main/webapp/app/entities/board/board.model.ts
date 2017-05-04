import { User } from '../../shared';
export class Board {
    constructor(
        public id?: number,
        public name?: string,
        public owner?: User,
    ) {
    }
}
