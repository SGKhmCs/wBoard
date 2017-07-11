export class Board {
    constructor(
        public id?: number,
        public name?: string,
        public pub?: boolean,
        public createdDate?: any,
        public bodyId?: number,
        public createdById?: number,
    ) {
        this.pub = false;
    }
}
