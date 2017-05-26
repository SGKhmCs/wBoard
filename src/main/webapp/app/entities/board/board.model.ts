export class Board {
    constructor(
        public id?: number,
        public name?: string,
        public pub?: boolean,
        public ownerId?: number,
    ) {
        this.pub = false;
    }
}
