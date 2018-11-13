import { SourcesModule } from './sources.module';

describe('SourcesModule', () => {
  let sourcesModule: SourcesModule;

  beforeEach(() => {
    sourcesModule = new SourcesModule();
  });

  it('should create an instance', () => {
    expect(sourcesModule).toBeTruthy();
  });
});
