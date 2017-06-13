export class Board {
    constructor(
        public id?: number,
        public name?: string,
        public pub?: boolean,
    ) {
        this.pub = false;
    }
}
