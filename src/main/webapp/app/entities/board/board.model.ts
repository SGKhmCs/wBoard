import { User } from '../../shared';
import { Reader } from '../reader';
import { Writer } from '../writer';
import { Admin } from '../admin';
export class Board {
    constructor(
        public id?: number,
        public name?: string,
        public pub?: boolean,
        public owner?: User,
        public reader?: Reader,
        public writer?: Writer,
        public admin?: Admin,
    ) {
        this.pub = false;
    }
}
