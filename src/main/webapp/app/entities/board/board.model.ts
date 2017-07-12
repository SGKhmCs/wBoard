export class Board {
    constructor(
        public id?: number,
        public name?: string,
        public pub?: boolean,
        public createdDate?: any,
        public createdBy?: string,
    ) {
        this.pub = false;
    }
}
