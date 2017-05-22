import { User } from '../../shared';
import { Board } from '../board';
import { PermissionType } from '../permission-type';
export class Permission {
    constructor(
        public id?: number,
        public user?: User,
        public board?: Board,
        public type?: PermissionType,
    ) {
    }
}
